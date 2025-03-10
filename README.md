# Projeto RPG: ShadowBlade

Bem-vindo ao repositório do **ShadowBlade**, um jogo RPG 2D desenvolvido em Java! Este projeto é um exemplo de como criar um jogo estilo RPG usando conceitos de programação orientada a objetos (POO), renderização gráfica, gerenciamento de estados e muito mais. Abaixo, você encontrará uma visão geral do projeto, suas funcionalidades e instruções para executá-lo.

---

## Visão Geral

O **ShadowBlade** é um jogo RPG 2D onde o jogador controla um personagem em um mundo repleto de monstros, NPCs (personagens não jogáveis) e desafios. O jogo inclui:

- **Exploração de Mapas**: O jogador pode se mover por diferentes mapas, interagir com NPCs e enfrentar inimigos.
- **Combate**: Encontros com monstros que reagem aos ataques do jogador.
- **Sistema de Vida**: O jogador possui uma barra de vida que diminui ao receber dano.
- **Interface de Usuário (UI)**: Menus interativos, diálogos, tela de título, opções e tela de fim de jogo.
- **Salvamento e Carregamento**: O progresso do jogador pode ser salvo e carregado.
- **Transições entre Mapas**: Efeitos visuais suaves ao mudar de mapa.

---

## Funcionalidades Principais

### 1. **Renderização Gráfica**
- O jogo utiliza `BufferedImage` e `Graphics2D` para renderizar gráficos na tela.
- Técnicas como **double buffering** são usadas para evitar flickering e garantir uma experiência visual suave.

### 2. **Gerenciamento de Estados**
- O jogo possui diferentes estados, como:
  - **Tela de Título**: Menu inicial com opções para começar um novo jogo, carregar um jogo salvo ou sair.
  - **Jogando**: Estado principal onde o jogador explora o mundo e enfrenta inimigos.
  - **Pausa**: Menu de pausa com opções para retomar o jogo ou acessar configurações.
  - **Diálogo**: Exibe conversas com NPCs.
  - **Fim de Jogo**: Tela exibida quando o jogador é derrotado, com opções para recomeçar ou voltar ao menu principal.

### 3. **Sistema de Combate e Vida**
- O jogador possui uma barra de vida que é exibida na tela.
- Monstros reagem ao ver o player, e parte para atacá-lo

### 4. **Salvamento e Carregamento**
- O progresso do jogador (vida, posição, estado dos monstros, etc.) pode ser salvo em um arquivo e carregado posteriormente.
- A classe `SaveLoad` gerencia a serialização e desserialização dos dados do jogo. 

### 5. **Interface de Usuário (UI)**
- A classe `UI` é responsável por renderizar todos os elementos visuais da interface, como:
  - Barra de vida.
  - Menus (título, pausa, opções).
  - Diálogos.
  - Telas de transição e fim de jogo.

---

## Inteligência Artificial (IA)
A IA no ShadowBlade é implementada para proporcionar uma experiência de jogo mais dinâmica e desafiadora. Aqui estão os principais aspectos da IA:

### 1. Pathfinding (Busca de Caminho)
- O algoritmo A* é usado para encontrar o caminho mais curto entre o monstro e o jogador.
- O algoritmo considera obstáculos (como paredes e outros objetos sólidos) para evitar que os monstros fiquem presos ou atravessem paredes.

### 2. Reações dos Monstros
- Perseguição: Quando o jogador está próximo, os monstros o perseguem usando o caminho calculado pelo algoritmo A*.
- Fuga: Se um monstro estiver com pouca vida, ele pode fugir do jogador.
- Reação a Dano: Monstros reagem ao receber dano, podendo mudar seu comportamento (por exemplo, fugir ou atacar com mais agressividade).

---

## Estrutura do Projeto

O projeto é organizado em pacotes que separam as responsabilidades:

- **`main`**: Contém a classe principal (`GamePanel`) e outras classes relacionadas ao loop do jogo e gerenciamento de estados.
- **`entity`**: Classes que representam entidades do jogo, como o jogador (`Player`), monstros e NPCs.
- **`object`**: Classes que representam objetos do jogo, como corações (vida).
- **`tile`**: Gerenciamento de tiles (blocos) que compõem os mapas.
- **`data`**: Classes para gerenciar salvamento e carregamento de dados (`SaveLoad`).
- **`ai`**: Implementação de inteligência artificial para monstros e NPCs.
- **`ui`**: Classes relacionadas à interface do usuário.

---

## Como Executar o Projeto

### Pré-requisitos
- **Java Development Kit (JDK)**: Certifique-se de ter o JDK instalado (versão 8 ou superior).
- **IDE**: Recomenda-se o uso de uma IDE como IntelliJ IDEA, Eclipse ou VS Code.

### Passos para Executar
1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/shadowblade-rpg.git
   ```
2. Abra o projeto em sua IDE.
3. Compile e execute a classe `Main` no pacote `main`.
4. Use as teclas do teclado para controlar o jogador e interagir com o jogo.

---

## Controles

- **W, A, S, D**: Movimentar o jogador e Navegar pelos menus.
- **Enter**: Atacar e Confirmar ações (iniciar jogo, selecionar opções, dialogar com NPCs).
- **Esc**: Pausar o jogo ou voltar ao menu anterior.

---
