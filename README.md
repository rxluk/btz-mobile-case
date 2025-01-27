# BTZ -Mobile - Case

---

## 1. Introdução

---

- O presente projeto consiste no desenvolvimento de um sistema de inventário de equipamentos para plataformas mobile (Android ou iOS), com o objetivo de demonstrar habilidades em desenvolvimento mobile, uso de QR codes, autenticação de usuários e gerenciamento de dados offline.
- O sistema proposto permitirá que os usuários realizem o controle e atualização de inventários de forma prática e eficiente, utilizando tecnologias que promovem a organização e acessibilidade de informações essenciais. Além disso, será fornecido um painel administrativo para gerenciar os vínculos entre equipamentos e usuários, garantindo flexibilidade e controle centralizado.

## 2. Histórias de Usuário

---

- As histórias de usuário descrevem de maneira objetiva as necessidades e objetivos dos usuários no sistema, considerando suas interações com o aplicativo.

| **ID** | **Descrição** |
| --- | --- |
| **HU01** | Como um usuário, eu quero acessar o sistema com meu nome de usuário e senha para garantir que apenas pessoas autorizadas possam visualizar e gerenciar os equipamentos vinculados à minha conta. |
| **HU02** | Como um usuário, eu quero ser redirecionado para a tela de listagem de equipamentos após realizar login bem-sucedido, para que eu possa visualizar rapidamente os equipamentos sob minha responsabilidade. |
| **HU03** | Como um usuário, eu quero visualizar uma lista dos equipamentos vinculados à minha conta, contendo informações como nome, código, data do último inventário e status de validade, para acompanhar a situação de cada equipamento. |
| **HU04** | Como um usuário, eu quero realizar um inventário escaneando o QR Code de um equipamento, para atualizar automaticamente a data do último inventário e garantir que ele esteja registrado como válido. |
| **HU05** | Como um usuário, eu quero ser notificado visualmente (por ícones ou cores) se o inventário de um equipamento está vencido ou válido, para saber quais equipamentos precisam de atenção. |
| **HU06** | Como um administrador, eu quero acessar uma tela dedicada à administração do sistema, para gerenciar e organizar os equipamentos vinculados a cada usuário. |
| **HU07** | Como um administrador, eu quero identificar equipamentos lendo um QR Code, para que o sistema reconheça o equipamento e permita sua associação com um usuário. |
| **HU08** | Como um administrador, eu quero vincular um equipamento identificado a um usuário específico, para garantir que ele apareça na lista de equipamentos do usuário selecionado. |
| **HU09** | Como um administrador, eu quero cadastrar novos equipamentos no sistema, utilizando a leitura de QR Codes, para que eles estejam disponíveis para gerenciamento e inventário. |

## 3. Levantamento de Requisitos

---

- O levantamento de requisitos é uma etapa fundamental no desenvolvimento de sistemas, pois busca identificar, documentar e validar as funcionalidades e características que o sistema deve atender. Esses requisitos servem como base para o planejamento, implementação e validação do projeto, garantindo que ele atenda às expectativas e necessidades dos usuários.

### 3.1 - Requisitos Funcionais

---

- Os requisitos funcionais descrevem o que o sistema deve fazer para atender às necessidades do usuário, incluindo suas funcionalidades e interações. Eles são diretamente derivados das histórias de usuário e representam ações ou processos que o sistema deve executar.

| **ID** | **Descrição** | **Referência (HU)** |
| --- | --- | --- |
| **RF01** | O sistema deve permitir que o usuário faça login com nome de usuário e senha. | **HU01** |
| **RF02** | O sistema deve validar as credenciais do usuário antes de conceder acesso. | **HU01** |
| **RF03** | O sistema deve redirecionar o usuário logado para a tela de listagem de equipamentos vinculados à sua conta. | **HU02** |
| **RF04** | O sistema deve exibir uma lista de equipamentos vinculados ao usuário logado, contendo nome, código e data. | **HU03** |
| **RF05** | O sistema deve indicar se o inventário do equipamento está válido ou vencido com base na data do último inventário. | **HU05** |
| **RF06** | O sistema deve permitir que o usuário realize um inventário por meio da leitura de QR Code de um equipamento. | **HU04** |
| **RF07** | O sistema deve atualizar a data do inventário para a data atual após a leitura bem-sucedida do QR Code. | **HU04** |
| **RF08** | O sistema deve atualizar automaticamente a lista de equipamentos após a realização de um inventário. | **HU04** |
| **RF09** | O sistema deve oferecer uma tela de administração para gerenciar equipamentos e usuários. | **HU06** |
| **RF10** | O sistema deve permitir que o administrador leia um QR Code para identificar um equipamento. | **HU07** |
| **RF11** | O sistema deve permitir que o administrador vincule um equipamento identificado a um usuário. | **HU08** |
| **RF12** | O sistema deve permitir que o administrador cadastre novos equipamentos no sistema por meio da leitura de QR Codes. | **HU09** |

## 4. Casos de Uso

---

- Os casos de uso descrevem as interações entre os atores (usuários e administradores) e o sistema, representando de forma clara as funcionalidades e os objetivos do sistema. Cada caso de uso é projetado para atender a uma necessidade específica, detalhando como o sistema deve reagir às ações do ator e quais funcionalidades são acionadas durante o processo.
- Esses casos de uso são representados graficamente através de diagramas e detalhados em etapas para fornecer uma visão completa das interações e dependências entre os componentes do sistema.

![Diagrama de Casos de Uso](/docs/UseCase.png)

## 5. Estrutura de Pacotes

---


- Aqui está a estrutura de pacotes do projeto:

com.example.btzmobileapp
|
├── config
|   ├── AppDatabase.java
|   └── DatabaseModule.java
|
├── module
|   ├── equipamento
|   |   ├── controller
|   |   |   ├── EquipamentoController.java
|   |   |
|   |   ├── dao
|   |   |   ├── EquipamentoDao.java
|   |   |
|   |   ├── domain
|   |   |   ├── Equipamento.java
|   |   |
|   |   ├── service
|   |   |   ├── EquipamentoService.java
|
|   ├── user
|   |   ├── controller
|   |   |   ├── UserController.java
|   |   |
|   |   ├── dao
|   |   |   ├── UserDao.java
|   |   |
|   |   ├── domain
|   |   |   ├── User.java
|   |   |
|   |   ├── service
|   |   |   ├── UserService.java
|
├── security
|   ├── EncryptionUtil.java
|
├── util
|   ├── DateConverter.java
|
├── views
|   ├── BaseActivity.java
|   ├── LoginActivity.java
|   ├── PerfilActivity.java
|   ├── HistoricoInventariosActivity.java
|   ├── ListEquipamentosActivity.java
|   ├── NovoInventarioActivity.java
|   └── user
|       ├── UserActivity.java
|   ├── admin
|   |   ├── AdminActivity.java
|   |   ├── CrudUserActivity.java
|   |   ├── CadastrarEquipamentoActivity.java
|   |   ├── LerEquipamentoActivity.java
|   |   └── VincEquipActivity.java
|
├── MainActivity.java
