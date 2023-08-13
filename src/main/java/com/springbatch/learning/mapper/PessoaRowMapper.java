package com.springbatch.learning.mapper;

import com.springbatch.learning.models.Pessoa;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PessoaRowMapper implements RowMapper<Pessoa> {

    @Override
    public Pessoa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Pessoa Pessoa = new Pessoa();
        Pessoa.setPessoaId(rs.getInt("pessoaId"));
        Pessoa.setNome(rs.getString("nome"));
        Pessoa.setDtNascimento(rs.getDate("dtNascimento").toLocalDate());
        Pessoa.setCpf(rs.getString("cpf"));
        Pessoa.setEnderecos("enderecos");
        return Pessoa;
    }

}
