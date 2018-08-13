package com.security.example.demo.comfig;

import com.security.example.demo.entity.Permission;
import com.security.example.demo.mapper.PermissionMapper;
import com.security.example.demo.mapper.UserMapper;
import com.security.example.demo.util.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    UserDetailsService customUserService() { //注册UserDetailsService 的bean
        return new CustomUserService();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //user Details Service验证
        auth.userDetailsService(customUserService()).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return Md5Util.getMd5((String) rawPassword);//;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(Md5Util.getMd5((String) rawPassword));
            }
        });
    }

    /***
     * 拦截设置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/403").permitAll()
                .antMatchers("/404").permitAll()
                .antMatchers("/500").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .anyRequest().authenticated() //任何请求,登录后可以访问
                .anyRequest().access("@rbacService.hasPermission(request, authentication)")
                .and().formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll(); //注销行为任意访问
    }


    @Service
    public class CustomUserService implements UserDetailsService { //自定义UserDetailsService 接口

        @Autowired
        UserMapper userMapper;
        @Autowired
        PermissionMapper permissionMapper;

        public UserDetails loadUserByUsername(String username) {
            com.security.example.demo.entity.User user = userMapper.findByUserName(username);
            if (user == null) {
                throw new UsernameNotFoundException("admin: " + username + " do not exist!");
            }

            List<Permission> permissions = permissionMapper.findByAdminUserId(user.getId());
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            if (permissions != null && permissions.size() > 0) {
                for (Permission permission : permissions) {
                    if (permission != null && permission.getName() != null) {
                        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
                        //1：此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
                        grantedAuthorities.add(grantedAuthority);
                    }
                }
            }

            //AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN)
            return new User(user.getUserName(), user.getPassWord(), grantedAuthorities);

        }

    }

    @Component("rbacService")
    public class RbacService {

        @Autowired
        PermissionMapper permissionMapper;

        private AntPathMatcher antPathMatcher = new AntPathMatcher();

        public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
            Object principal = authentication.getPrincipal();
            boolean hasPermission = false;
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                List<Permission> permissions = permissionMapper.findByAdminUserName(username);
                if (permissions == null || permissions.size() == 0)
                    return false;

                for (Permission permission : permissions) {

                    // 判断当前url是否有权限
                    if (antPathMatcher.match(permission.getUrl(), request.getRequestURI())) {
                        hasPermission = true;
                        break;
                    }
                }
            }
            return hasPermission;
        }
    }


}