# spring-boot

Projeto de aprendizado do framework java Spring Boot.
É baseado no projeto base [Cursomc](https://github.com/icarodebarros/cursomc) somado à melhorias de código (crição de classes genéricas).

### Requisitos

 - Spring Boot Verion 2.2.5;
 - Spring Web;

### `TODO` list

 - Criar maneira genérica de trazer nas buscas (ex. findAll()) apenas os campos necessários. Atualmente isso está sendo feito manualmente;
 - Implementar classes 'Filtros' para as requisições de busca dos objetos. Criar também o GenericFilter contendo atributos de busca genéricos como quais campos dos objetos devem ser retornados, além de atributos de paginação como page, linesPerPage, orderBy e direction;
 - Adicionar mais atributos no Pojo (GenericDomain) como data de criação/alteração, usuário que fez alteração, etc;
