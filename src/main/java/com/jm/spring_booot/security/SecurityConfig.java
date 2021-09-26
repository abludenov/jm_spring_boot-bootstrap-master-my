package com.jm.spring_booot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //приватные переменные LoginSuccessHandler handler UserDetailsService service
    //и конструктор с этими параметрами на вход
    private final UserDetailsService userDetailsService;
    // сервис, с помощью которого тащим пользователя
    private final SuccessUserHandler successUserHandler;
    // класс, в котором описана логика перенаправления пользователей по ролям

    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                          SuccessUserHandler successUserHandler) {
        this.userDetailsService = userDetailsService;
        this.successUserHandler = successUserHandler;
    }
    //Одним из важных методов в классе SecurityConfig
    // является configureGlobalSecurity ;
    // в рамках этого метода мы просто настраиваем
    // AuthenticationManagerBuilder для создания двух
    // пользователей user и admin с заданным паролем и ролями.

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        // конфигурация для прохождения аутентификации
        //тут для аутентификации нам нужно чтобы юзердитейлс информацию передавал
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedPage("/error_access");

        http.formLogin()//стандартная форма от спринга
                // указываем страницу с формой логина
                //.loginPage("/login")
                //указываем логику обработки при логине
                .successHandler(successUserHandler)
                // указываем action с формы логина
                .loginProcessingUrl("/login")
                // Указываем параметры логина и пароля с формы логина
                //.usernameParameter("j_username")
                //.passwordParameter("j_password")
                // даем доступ к форме логина всем
                .permitAll();


        //если .httpBasic() нового юзера автоматически перекидывает на окно с аутентификацией
        //либо указать .formLogin() и там указать где искать форму, куда она ведет, как выглядит и тп
        //если пустые скобки то будет стандартная форма от спринга
        //либо .logout(),тут указали URL логаута и URL при удачном логауте
        http.logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/login")
                //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
                .and().csrf().disable();

        http
                // делаем страницу регистрации недоступной для авторизированных пользователей
                .authorizeRequests()
                //страницы аутентификаци доступна всем
                .antMatchers("/login").anonymous()
                // защищенные URL
                .antMatchers("/admin/**").access("hasAnyRole('ADMIN')")
                .antMatchers("/user/**").access("hasAnyRole('USER','ADMIN')");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

