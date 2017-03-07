package br.edu.ufcg.computacao.si1.model.form;


import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AnuncioForm {

    private static final String[] tipos = new String[] {"movel", "imovel", "emprego"};

    @NotNull(message = "O titulo não pode ser nulo.")
    @NotEmpty(message = "O titulo não pode esta vazio.")
    @Size(min = 10, max = 100, message = "O titulo deve ter entre 2 e 100 caracters")
    private String titulo;
    @NotNull(message = "O preçço não pode ser nulo.")
    @DecimalMin(value = "0.1", message = "O preço minimo é 0.1 para um anúncio.")
    private Double preco;
    @NotNull(message = "O tipo de anúncio não pode ser nulo.")
    @NotEmpty(message = "Escolha um tipo para o anúncio.")
    private String tipo;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public static String[] getTipos() {
        return tipos;
    }
}

