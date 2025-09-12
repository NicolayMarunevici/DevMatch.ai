//package com.devmatch.gateway.gateway.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.JwtParser;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import jakarta.annotation.PostConstruct;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.http.HttpCookie;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//public class JwtAuthCookieFilter implements GlobalFilter {
//  private final String securityKey;
//  private JwtParser jwtParser;
//
//  public JwtAuthCookieFilter(@Value("${jwt.secret}") String securityKey) {
//    this.securityKey = securityKey;
//  }
//
//  @PostConstruct
//  public void init(){
//    this.jwtParser = Jwts.parserBuilder()
//        .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(securityKey))).build();
//  }
//
//  private static final List<String> WHITELIST = List.of(
//      "/auth/login",
//      "/auth/register",
//      "/auth/refresh"
//  );
//
//  @Override
//  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//    String path = exchange.getRequest().getURI().getPath();
//
//    if(WHITELIST.stream().anyMatch(path::startsWith)){
//      return chain.filter(exchange);
//    }
//
//    HttpCookie accessCookie = exchange.getRequest().getCookies().getFirst("accessToken");
//
//    if(accessCookie == null){
//      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//      return exchange.getResponse().setComplete();
//    }
//
//    try {
//      Claims claims = jwtParser.parseClaimsJws(accessCookie.getValue()).getBody();
//
//      List<String> roles = claims.get("role", List.class);
//      String rolesString = String.join(",", roles);
//
//      ServerHttpRequest mutateRequest = exchange.getRequest().mutate()
//          .header("X-User-Id", claims.get("userId").toString())
//          .header("X-User-Role", rolesString)
//          .build();
//
//      return chain.filter(exchange.mutate().request(mutateRequest).build());
//    } catch (JwtException e){
//      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//      return exchange.getResponse().setComplete();
//    }
//  }
//}
