# DESENVOLVIMENTO DE SOFTWARE PARA A WEB 1
Prof. Delano M. Beder (UFSCar)

## Atividade AA-1: Sistema para Contratação de Desenvolvedores (Freelancers)

O sistema deve possuir um cadastro de desenvolvedores, com os seguintes dados: e-mail, senha, CPF, nome, telefone, sexo e data de nascimento.
O sistema deve possuir um cadastro de empresas, com os seguintes dados: e-mail, senha, CNPJ, nome e descrição.

O sistema deve atender aos seguintes requisitos:
* **R1:** CRUD de desenvolvedores (requer login de administrador).
* **R2:** CRUD de empresas (requer login de administrador).
* **R3:** Cadastro de projeto/vaga para desenvolvimento (requer login da empresa via e-mail + senha). 
  Depois de fazer login, a empresa pode cadastrar um projeto em busca de profissionais. O cadastro de projetos deve possuir os seguintes dados: CNPJ da empresa, título do projeto, descrição detalhada (requisitos, stack tecnológica esperada), orçamento estimado, prazo de entrega e anexos (no máximo 10 imagens de mockups ou diagramas do escopo).
* **R4:** Listagem de todos os projetos em uma única página (não requer login). 
  O sistema deve prover a funcionalidade de filtrar os projetos por stack tecnológica/palavra-chave.
* **R5:** Proposta de prestação de serviço (requer login do desenvolvedor via e-mail + senha). 
  Ao clicar em um projeto (requisito R4), o desenvolvedor pode realizar uma proposta de desenvolvimento. Nesse caso, é necessário que ele apresente o valor da sua proposta, o prazo estimado para entrega e uma justificativa técnica/portfólio. A data atual (quando foi realizada a proposta) deve ser registrada no sistema. O sistema deve restringir que cada desenvolvedor tenha apenas uma proposta em aberto (requisito R7) para cada projeto.
* **R6:** Listagem de todos os projetos de uma empresa (requer login da empresa via e-mail + senha). 
  Depois de fazer login, a empresa pode visualizar todos os seus projetos cadastrados.
* **R7:** Listagem de todas as propostas de um desenvolvedor (requer login do desenvolvedor via e-mail + senha). 
  Depois de fazer login, o desenvolvedor pode visualizar todas as suas propostas cadastradas com seus respectivos status:
    * **ABERTO:** indica que ainda encontra-se em fase de análise pela empresa (requisito R8).
    * **NÃO ACEITO:** indica que a empresa não aceitou a proposta de desenvolvimento.
    * **ACEITO:** indica que a empresa aceitou a proposta.
* **R8:** A empresa (requer login da empresa via e-mail + senha), para cada proposta de desenvolvimento, deve analisar as condições da proposta (valor, prazo, etc.) e atualizar o status para NÃO ACEITO ou ACEITO. 
  Nos dois casos, o desenvolvedor deve ser informado (via e-mail) sobre a decisão.
    * No caso do status **NÃO ACEITO**, a empresa pode informar opcionalmente uma contraproposta (novo valor ou escopo ajustado) no corpo da mensagem enviada.
    * No caso do status **ACEITO**, a empresa deve também informar um horário para uma reunião (via videoconferência) com o desenvolvedor para alinhar o início do projeto — o link da videoconferência (Google Meet, Zoom, etc.) deve estar presente no corpo da mensagem enviada.
* **R9:** O sistema deve ser internacionalizado em pelo menos dois idiomas: português + outro de sua escolha.
* **R10:** O sistema deve validar (tamanho, formato, etc.) todas as informações (campos nos formulários) cadastradas e/ou editadas. 
  O sistema deve tratar todos os erros possíveis (cadastros duplicados, problemas técnicos, etc.) mostrando uma página de erros amigável ao usuário e registrando o erro no console.

### Arquitetura e Tecnologias
* **Padrão:** Modelo-Visão-Controlador (MVC)
* **Lado Servidor:** Spring MVC, Spring Data JPA, Spring Security & Thymeleaf
* **Lado Cliente:** JavaScript & CSS

### Ambiente de Desenvolvimento
* A compilação e o deployment devem ser obrigatoriamente realizados via Maven.
* Os arquivos fonte do sistema devem estar hospedados obrigatoriamente em um repositório (preferencialmente GitHub).

---

## Atividade AA-2: Sistema para Contratação de Desenvolvedores (Freelancers)

**Obs 1:** Essa atividade deve ser baseada na atividade AA-1. Ou seja, deve-se apenas implementar os novos requisitos (funcionalidades providas em uma REST API) aqui mencionados — levando em consideração o que já foi desenvolvido na atividade AA-1.

O sistema deve incorporar os seguintes requisitos:

### REST API - CRUD de Desenvolvedores
* **Cria um novo desenvolvedor [Create]:** `POST http://localhost:8080/api/desenvolvedores` (Body: raw/JSON)
* **Retorna a lista de desenvolvedores [Read]:** `GET http://localhost:8080/api/desenvolvedores`
* **Retorna o desenvolvedor de id = {id} [Read]:** `GET http://localhost:8080/api/desenvolvedores/{id}`
* **Atualiza o desenvolvedor de id = {id} [Update]:** `PUT http://localhost:8080/api/desenvolvedores/{id}` (Body: raw/JSON)
* **Remove o desenvolvedor de id = {id} [Delete]:** `DELETE http://localhost:8080/api/desenvolvedores/{id}`

### REST API - CRUD de Empresas
* **Cria uma nova empresa [Create]:** `POST http://localhost:8080/api/empresas` (Body: raw/JSON)
* **Retorna a lista de empresas [Read]:** `GET http://localhost:8080/api/empresas`
* **Retorna a empresa de id = {id} [Read]:** `GET http://localhost:8080/api/empresas/{id}`
* **Atualiza a empresa de id = {id} [Update]:** `PUT http://localhost:8080/api/empresas/{id}` (Body: raw/JSON)
* **Remove uma empresa de id = {id} [Delete]:** `DELETE http://localhost:8080/api/empresas/{id}`

### REST API - Endpoints de Projetos e Propostas
* **Retorna a lista de propostas de desenvolvimento do projeto de id = {id}:** `GET http://localhost:8080/api/propostas/projetos/{id}`
* **Retorna a lista de propostas do desenvolvedor de id = {id}:** `GET http://localhost:8080/api/propostas/desenvolvedores/{id}`
* **Cria um novo projeto na empresa de id = {id}:** `POST http://localhost:8080/api/projetos/empresas/{id}` (Body: raw/JSON)
* **Retorna a lista de projetos da empresa de id = {id}:** `GET http://localhost:8080/api/projetos/empresas/{id}`
* **Retorna a lista de projetos filtrados por stack tecnológica cujo nome = {nome}:** `GET http://localhost:8080/api/projetos/stacks/{nome}`

**Obs 2:** Em todas as funcionalidades mencionadas na REST API (AA-2), não há necessidade de autenticação (login).

**Dica:** Na configuração do Spring Security utilize algo semelhante ao apresentado no código abaixo:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests((authz) -> authz
        // linhas anteriores
        .requestMatchers("/api/**").permitAll()
        .anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin((form) -> form
            .loginPage("/login")
            .permitAll())
        .logout((logout) -> logout
            .logoutSuccessUrl("/").permitAll());
    return http.build();
}