<p align="center">
  <img src="https://github.com/Lorrane-Maria/Organizo_Backend/blob/main/Organizo-removebg-preview.png" />
</p>

# Organizo Backend

API RESTful para o projeto **Organizo**, uma plataforma de gerenciamento para salões de beleza. Este projeto foi desenvolvido como parte da API de Autenticação e Autorização JWT (Emissão e Validação Interna) e AV2, com foco em segurança com JWT, testes unitários e de integração, monitoramento com Prometheus/Grafana e deploy com Docker.

---

## 🚀 Tecnologias Utilizadas

- **Java 21** & **Spring Boot 3.x**
- **Spring Security** com autenticação e autorização via **JWT**
- **Spring Data JPA** com **Hibernate**
- **Banco de Dados H2** (em memória) e suporte a **MySQL**
- **Spring Boot Actuator** para métricas de saúde e performance
- **Prometheus & Grafana** para monitoramento em tempo real
- **Docker & Docker Compose** para containerização e orquestração
- **Maven** para gerenciamento de dependências
- **JUnit 5 & Mockito** para testes
- **Springdoc OpenAPI (Swagger)** para documentação de API

---

## ⚙️ Pré-requisitos

- [JDK 21+](https://www.oracle.com/java/technologies/downloads/)
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- [Docker & Docker Compose](https://www.docker.com/products/docker-desktop/)
- [Apache JMeter](https://jmeter.apache.org/download_jmeter.cgi) (para testes de carga)

---

## 🏁 Como Executar

### 1. Usando Maven (Aplicação Isolada)

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/organizobackend.git
cd organizobackend

# Compile e rode os testes para garantir a integridade
mvn clean install

# Inicie a aplicação
mvn spring-boot:run
```
A API estará disponível em `http://localhost:8080`.

### 2. Usando Docker Compose (Ambiente Completo: API + Prometheus + Grafana)

Este é o método recomendado para simular um ambiente de produção.

```bash
# Na raiz do projeto, suba todos os serviços definidos no docker-compose.yml
docker-compose up --build
```
*O comando `--build` força a reconstrução da imagem da sua API se houver mudanças.*

<details>
<summary>💡 Clique para ver o arquivo <code>docker-compose.yml</code> de exemplo</summary>

```yaml
# docker-compose.yml
version: '3.8'

services:
  # Serviço da sua API Spring Boot
  organizobackend:
    build: . # Constrói a imagem a partir do Dockerfile na raiz
    container_name: organizo-backend-app
    ports:
      - "8080:8080"
    environment:
      # Passe variáveis de ambiente para o container
      - JWT_SECRET=minha_chave_super_secreta_para_o_compose_123
      - JWT_EXPIRATION=86400000 # 24 horas
    networks:
      - organizo-net

  # Serviço do Prometheus para coletar métricas
  prometheus:
    image: prom/prometheus:v2.47.2
    container_name: prometheus
    ports:
      - "9090:9090"
    volumes:
      # Mapeia o seu arquivo de configuração local para dentro do container
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
    networks:
      - organizo-net

  # Serviço do Grafana para visualizar as métricas
  grafana:
    image: grafana/grafana-oss:10.2.2
    container_name: grafana
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_USER=admin
      - GF_SECURITY_ADMIN_PASSWORD=admin
    networks:
      - organizo-net

networks:
  organizo-net:
    driver: bridge
```
</details>

---

## 📖 Acessando os Serviços

- **API Organizo:** `http://localhost:8080`
- **Documentação Swagger:** `http://localhost:8080/swagger-ui.html`
- **Console H2:** `http://localhost:8080/h2-console`
  - **JDBC URL:** `jdbc:h2:mem:testdb`
  - **User Name:** `sa`
  - **Password:** (deixe em branco)
- **Prometheus Dashboard:** `http://localhost:9090`
- **Grafana Dashboard:** `http://localhost:3000` (user: `admin`, pass: `admin`)

---

## ✅ Testes e Qualidade

- **Executar todos os testes:** `mvn test`
- **Gerar relatório de cobertura (JaCoCo):** `mvn clean verify` (requer plugin no `pom.xml`)
- **Teste de Carga:** Siga as instruções no seu arquivo `.jmx` usando o Apache JMeter.

---

## 🛡️ Segurança

- **Segredos:** A chave JWT e outras senhas são gerenciadas via variáveis de ambiente, conforme `application.yml`. **Nunca** comite segredos no código.
- **Acesso em Produção:** Em um ambiente produtivo, o acesso aos endpoints `/h2-console` e `/swagger-ui.html` deve ser desabilitado ou protegido por um perfil de `admin`.
```
## 🧑‍💻 DESENVOLVEDORES
Maurício Antônio Theodoro Neto ([Github](https://github.com/mauricio-theodoro//)) / Contato: mauricioantonionetinho@gmail.com
Paulo Henrique Vieira ([Github](https://github.com/Paulotjcouto//)) / Contato: paulohenriquecouto2001@gmail.com
Lorrane Maria ([Linkedin](https://www.linkedin.com/in/lorrane-maria-5396b021b/)) / Contato: lorranemaria57@gmail.com
