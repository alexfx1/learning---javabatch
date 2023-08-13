package com.springbatch.learning.config;

import com.springbatch.learning.mapper.PessoaRowMapper;
import com.springbatch.learning.models.Pessoa;
import com.springbatch.learning.service.PessoaItemProcessor;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Autowired
    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcCursorItemReader<Pessoa> reader() {
        JdbcCursorItemReader<Pessoa> reader = new JdbcCursorItemReader<Pessoa>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT pessoa_id as pessoaId, nome, dt_nascimento as dtNascimento, cpf, enderecos FROM pedidos.pessoa");
        reader.setRowMapper(new PessoaRowMapper());

        return reader;
    }


    @Bean
    public PessoaItemProcessor processor(){
        return new PessoaItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<Pessoa> writer(){
        FlatFileItemWriter<Pessoa> writer = new FlatFileItemWriter<Pessoa>();
        writer.setResource(new FileSystemResource("C:\\Users\\alexs\\Desktop\\config_defender\\pessoas.csv"));
        writer.setLineAggregator(new DelimitedLineAggregator<Pessoa>() {{
            setDelimiter(",");
            setFieldExtractor(new BeanWrapperFieldExtractor<Pessoa>() {{
                setNames(new String[] { "pessoaId", "nome", "dtNascimento", "cpf", "enderecos" });
            }});
        }});

        return writer;
    }




    @Bean
    public Step step(ItemReader<Pessoa> reader, ItemWriter<Pessoa> writer) {
        return stepBuilderFactory.get("step")
                .<Pessoa, Pessoa> chunk(200)
                .reader(reader)
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public Job exportUserJob(Step step) {
        return jobBuilderFactory.get("exportPessoaJob")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }
}
