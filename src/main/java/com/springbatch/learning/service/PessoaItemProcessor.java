package com.springbatch.learning.service;

import com.springbatch.learning.models.Pessoa;
import org.springframework.batch.item.ItemProcessor;

public class PessoaItemProcessor implements ItemProcessor<Pessoa, Pessoa> {

    @Override
    public Pessoa process(Pessoa pessoa) throws Exception {
        return pessoa;
    }
}
