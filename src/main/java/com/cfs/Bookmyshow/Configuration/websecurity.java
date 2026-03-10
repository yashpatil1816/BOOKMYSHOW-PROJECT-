//package com.cfs.Bookmyshow.Configuration;
//
//import com.cfs.Bookmyshow.Service.CustomerUserdeatilService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class websecurity {
//
//
//    private final CustomerUserdeatilService customUserDetailService;
//
//    public websecurity(CustomerUserdeatilService customUserDetailService) {
//        this.customUserDetailService = customUserDetailService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
//        http
//                .csrf(csrf->csrf.disable())
//                .authorizeHttpRequests(auth->auth
//                        .requestMatchers("/api/booking/create","/api/booking/{id}").hasRole("USER")
//                        .requestMatchers("/api/movie/create","/api/user/list").hasRole("ADMIN")
//                        .requestMatchers("/api","/api/movie/list","/api/movie/{id}","/api/user/create","/api/user/{id}").permitAll()
//                        .anyRequest().authenticated()
//                ).httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }
//
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        AuthenticationManager authenticationManager = config.getAuthenticationManager();
//        System.out.println("AuthenticationManager called.......");
//        return authenticationManager;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder()
//    {
//        BCryptPasswordEncoder cryptPasswordEncoder = new BCryptPasswordEncoder();
//        System.out.println("passwordEncoder method called....");
//        return cryptPasswordEncoder;
//    }
//}
