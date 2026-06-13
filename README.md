UnB - Universidade de Brasilia  
FCTE - Faculdade de Ciências e Tecnologias em Engenharias  
TPPE - Técnicas de Programação para Plataformas Emergentes  
---

# TPPE-01-26-Trabalho-1
Esse repositório visa realizar a confecção do trabalho 1 da disciplina Técnicas de Programação em Plataformas Emergentes, no semestre 01/2026 do calendário da UnB, ministrado pelo docente  [Andre Luiz Peron Martins Lanna](https://github.com/andrelanna).

O enunciado do trabalho pode ser encontrado no link abaixo, onde estão descritos os requisitos, critérios de avaliação e orientações para a entrega:
[Enunciado do Trabalho 1 - TPPE](https://github.com/andrelanna/fga0242/tree/master/trabalhoPratico)


##  Tecnologias e Ferramentas Utilizadas

O projeto foi construído utilizando o ecossistema moderno da linguagem **Java**:

* **Linguagem:** Java 17 / Java 21 (LTS)
* **Gerenciador de Dependências e Build:** Apache Maven (v3.9+)
* **Framework de Testes:** JUnit 5 (Jupiter)
* A base do projeto foi desenvolvido no **VSCode**

---

##  Como Executar o Código e os Testes

Como o projeto utiliza o Apache Maven, todos os ciclos de compilação, gerenciamento de bibliotecas e execução de testes automatizados são centralizados em comandos simples no terminal.

### Pré-requisitos
Certifique-se de ter o **JDK 17 ou superior** (preferencialmente o Java 21) e o **Apache Maven** instalados em sua máquina.

### 1. Clonar o Repositório e Acessar a Pasta do Projeto
``` bash
git clone [https://github.com/JoelSRangel/TPPE-01-26-Trabalho-1.git](https://github.com/JoelSRangel/TPPE-01-26-Trabalho-1.git)
cd TPPE-01-26-Trabalho-1/trabalho1
```

### 2. Executar os Testes Automatizados

Para rodar toda a suíte de testes unitários desenvolvida sob a metodologia TDD, execute:

``` mvn test ```

Este comando irá baixar automaticamente as dependências do JUnit 5 (se for a primeira execução), compilar o código de produção e de testes, executá-los e exibir o relatório de sucesso (BUILD SUCCESS) ou falhas diretamente no console.

### 3. Limpar Arquivos de Build Anteriores

Caso queira apagar a pasta target com os arquivos compilados anteriormente para garantir um build totalmente limpo do zero, utilize:

``` mvn clean ```

### 4. Compilar o Projeto (Sem rodar os testes)

Se desejar apenas verificar se o código de produção está compilando sem erros de sintaxe ou tipagem:

``` mvn compile ```