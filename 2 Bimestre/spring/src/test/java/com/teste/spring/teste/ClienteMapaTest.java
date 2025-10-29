
package com.teste.spring.teste;

import com.teste.spring.teste.dto.ClienteDto;
import com.teste.spring.teste.mapa.ClienteMapa;
import com.teste.spring.teste.model.Cliente;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteMapaTest {

    @Test
    void toDTO_deveMapearTodosCampos() {
        Cliente entity = new Cliente();
        entity.setId(10L);
        entity.setNome("Ana");
        entity.setEmail("ana@teste.com");
        entity.setTelefone("1199999-0000");
        entity.setCriadoEm(LocalDateTime.now());

        ClienteDto dto = ClienteMapa.toDTO(entity);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getNome()).isEqualTo("Ana");
        assertThat(dto.getEmail()).isEqualTo("ana@teste.com");
        assertThat(dto.getTelefone()).isEqualTo("1199999-0000");
    }

    @Test
    void toEntity_deveMapearTodosCampos() {
        ClienteDto dto = new ClienteDto();
        dto.setId(20L);
        dto.setNome("Teste DTO");
        dto.setEmail("dto@teste.com");
        dto.setTelefone("888888888");

        Cliente entity = ClienteMapa.toEntity(dto);

        assertThat(entity.getId()).isEqualTo(20L);
        assertThat(entity.getNome()).isEqualTo("Teste DTO");
        assertThat(entity.getEmail()).isEqualTo("dto@teste.com");
        assertThat(entity.getTelefone()).isEqualTo("888888888");
        assertThat(entity.getCriadoEm()).isNull();
    }

    @Test
    void copyToEntity_deveCopiarCamposDeDados() {
        ClienteDto dto = new ClienteDto();
        dto.setNome("Nome Novo");
        dto.setEmail("novo@email.com");
        dto.setTelefone("777777777");

        Cliente entity = new Cliente();
        entity.setId(30L);
        entity.setNome("Nome Antigo");
        entity.setEmail("antigo@email.com");
        entity.setCriadoEm(LocalDateTime.now());

        ClienteMapa.copyToEntity(dto, entity);

        assertThat(entity.getId()).isEqualTo(30L);
        assertThat(entity.getNome()).isEqualTo("Nome Novo");
        assertThat(entity.getEmail()).isEqualTo("novo@email.com");
        assertThat(entity.getTelefone()).isEqualTo("777777777");
        assertThat(entity.getCriadoEm()).isNotNull();
    }
}