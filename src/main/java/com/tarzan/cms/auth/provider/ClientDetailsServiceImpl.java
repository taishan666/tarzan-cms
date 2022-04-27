package com.tarzan.cms.auth.provider;


import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class ClientDetailsServiceImpl implements IClientDetailsService {
    private final JdbcTemplate jdbcTemplate;

    public IClientDetails loadClientByClientId(String clientId) {
        try {
            return (IClientDetails)this.jdbcTemplate.queryForObject("select client_id, client_secret, access_token_validity, refresh_token_validity from blade_client where client_id = ?", new String[]{clientId}, new BeanPropertyRowMapper(ClientDetails.class));
        } catch (Exception var3) {
            return null;
        }
    }

    public ClientDetailsServiceImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
