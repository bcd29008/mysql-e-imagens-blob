# Inserindo e recuperando imagens no MySQL

Um pequeno exemplo para demonstrar como inserir e recuperar imagens (arquivo binário) em um banco de dados MySQL.

Crie uma tabela com base na instruções SQL abaixo:

```sql
CREATE TABLE IF NOT EXISTS Cidade (
  idCidade INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(45) NOT NULL,
  pais VARCHAR(45) NOT NULL,
  foto LONGBLOB NULL,
  PRIMARY KEY (idCidade))
```

## Executando o projeto

Antes de executar o projeto é necessário que atualize as informações para conexão no banco de dados MySQL no arquivo [application.properties](app/build/resources/main/application.properties).

É possível executar esse projeto pela IDE (i.e. VSCode, IntelliJ, etc), neste caso abra a classe [App.java](app/src/main/java/engtelecom/bcd/App.java), ou por linha de comando com o gradle. Neste caso, executando o comando: `./gradlew run` (no Linux ou macOS) ou `gradlew.bat run` no Windows.

### Requisitos
- JDK 11
- Gradle

# Referências

As [imagens](fotos) usadas neste exemplo foram obtidas no site [Unsplash](https://unsplash.com).