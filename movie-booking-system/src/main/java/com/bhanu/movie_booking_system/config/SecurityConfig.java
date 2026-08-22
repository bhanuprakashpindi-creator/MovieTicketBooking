    package com.bhanu.movie_booking_system.config;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Autowired
        public UserDetailsService userDetailsService;
        @Autowired
        private  JwtFilter jwtFilter;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(12);
        }
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(customizer -> customizer.disable());
            http.authorizeHttpRequests(request -> request
                    .requestMatchers(HttpMethod.POST,"/api/auth/register","/api/auth/login")
                    .permitAll()

                            // ================= SWAGGER =================

                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/v3/api-docs/**"
                            )
                            .permitAll()

                    // ================= MOVIE =================

                    // Admin
                    .requestMatchers(HttpMethod.POST, "/api/movies/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/api/movies/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.DELETE, "/api/movies/**")
                    .hasRole("ADMIN")

                    // User + Admin
                    .requestMatchers(HttpMethod.GET, "/api/movies/**")
                    .hasAnyRole("USER", "ADMIN")


                    // ================= THEATER =================

                    // Admin
                    .requestMatchers(HttpMethod.POST, "/api/theaters/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/api/theaters/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.DELETE, "/api/theaters/**")
                    .hasRole("ADMIN")

                    // User + Admin
                    .requestMatchers(HttpMethod.GET, "/api/theaters/**")
                    .hasAnyRole("USER", "ADMIN")


                    // ================= SCREEN =================

                    // Admin
                    .requestMatchers(HttpMethod.POST, "/api/screens/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/api/screens/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.DELETE, "/api/screens/**")
                    .hasRole("ADMIN")

                    // User + Admin
                    .requestMatchers(HttpMethod.GET, "/api/screens/**")
                    .hasAnyRole("USER", "ADMIN")


                    // ================= SEAT =================

                    // Admin
                    .requestMatchers(HttpMethod.POST, "/api/seats/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/api/seats/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.DELETE, "/api/seats/**")
                    .hasRole("ADMIN")

                    // User + Admin
                    .requestMatchers(HttpMethod.GET, "/api/seats/**")
                    .hasAnyRole("USER", "ADMIN")


                    // ================= SHOW =================

                    // Admin
                    .requestMatchers(HttpMethod.POST, "/api/shows/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/api/shows/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.DELETE, "/api/shows/**")
                    .hasRole("ADMIN")

                    // User + Admin
                    .requestMatchers(HttpMethod.GET, "/api/shows/**")
                    .hasAnyRole("USER", "ADMIN")


                    // ================= BOOKING =================

                    // USER + ADMIN can book
                    .requestMatchers(HttpMethod.POST, "/api/bookings/**")
                    .hasAnyRole("USER", "ADMIN")

                    // USER + ADMIN can view bookings
                    .requestMatchers(HttpMethod.GET, "/api/bookings/**")
                    .hasAnyRole("USER", "ADMIN")

                    // USER + ADMIN can cancel
                    .requestMatchers(HttpMethod.PUT, "/api/bookings/**")
                    .hasAnyRole("USER", "ADMIN")

                    // If your cancel endpoint uses DELETE
                    .requestMatchers(HttpMethod.DELETE, "/api/bookings/**")
                    .hasAnyRole("USER", "ADMIN")


                    // ================= DEFAULT =================

                    .anyRequest().authenticated())
                    .oauth2Login(Customizer.withDefaults());
    //        http.formLogin(Customizer.withDefaults());
            http.httpBasic(Customizer.withDefaults());
            http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }

        @Bean
        public AuthenticationProvider authenticationProvider ()
        {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setPasswordEncoder(passwordEncoder());
            provider.setUserDetailsService(userDetailsService);
            return provider;
        }
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }


    }
