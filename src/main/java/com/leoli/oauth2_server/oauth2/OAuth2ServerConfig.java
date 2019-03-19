package com.leoli.oauth2_server.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2ServerConfig extends
        AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public TokenStore memoryTokenStore() {
        return new InMemoryTokenStore();
    }

    @Override
    public void configure(
            AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
//        endpoints
//                .authenticationManager(authenticationManager)
//                .tokenStore(tokenStore())
//                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .userDetailsService(userDetailsService)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST, HttpMethod.GET);
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(connectionFactory);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        clients.inMemory()
                .withClient("leo")
                .secret("{bcrypt}" + passwordEncoder.encode("123456"))
                .scopes("restapi")
                .authorizedGrantTypes("password","authorization_code", "refresh_token")
                .redirectUris("http://authtest.com") //authorization_code模式需要
                .resourceIds();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //tokenKeyAccess是公钥的访问权限，和jw一起使用， checkTokenAccess是check token access，两个的默认值都是denyAll()"
//        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();

        // allowFormAuthenticationForClients==true时在AuthorizationServerSecurityConfigurer的config中添加一个filter
        // 这个filter时验证client的，不设置为true会获取不了token
        security.allowFormAuthenticationForClients();
    }

}
