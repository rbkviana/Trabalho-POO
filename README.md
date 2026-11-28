# Cabe no Bolso — Planejador de despesas compartilhadas

Aplicação desktop em **Java Swing** para organizar um passeio ou atividade em grupo: cadastra participantes e despesas, rateia os custos entre os envolvidos e mostra se cada pessoa ficou dentro ou acima do orçamento.

Atividade final da disciplina de **Programação Orientada a Objetos** — EAD Unifor, 2026.

## Equipe

| Nome completo | Matrícula | Contribuição |
|---|---|---|
| Marcelo Anderson Silva Peixoto | 2641608 | Modelagem do sistema e diagrama de classes UML |
| Kauã David Correia de Sousa | 2641681 | Classes de despesas, cálculos de rateio, herança e polimorfismo |
| Antony Italo da Silva Guedes | 2641605 | Interface gráfica em Swing e integração das camadas MVC |
| Gustavo Nogueira de Freitas | 2641701 | Persistência em arquivo, evidências e documentação técnica |
| Leticia Rebeca de Castro Viana | 2641588 | Revisão das classes de participantes e planejamento |
| Reryson Weslley de Moraes Pereira | 2641587 | Revisão dos serviços, validações e regras de negócio |

Todos os integrantes participaram da integração e da validação da aplicação.

## Funcionalidades

- **Participantes:** cadastrar (código único, nome, orçamento), editar nome e orçamento, excluir (bloqueado se houver despesas vinculadas).
- **Despesas:** incluir, editar e excluir despesas do tipo **fixa** (valor dividido entre os selecionados) ou **por pessoa** (valor cobrado de cada selecionado).
- **Rateio:** divisão com `BigDecimal`; os centavos que sobram vão para os participantes de menor código.
- **Resumo:** lista de despesas, custo total, total por participante e situação em relação ao orçamento (dentro ou excedido, com o valor).
- **Persistência:** salvar e carregar o planejamento completo por serialização de objetos.

## Requisitos

- **JDK 21** (versão usada no desenvolvimento; JDK 17 ou superior deve funcionar).
- Nenhuma dependência externa: só a biblioteca padrão do Java (Swing, `java.math`, `java.io`).
- Git, para clonar o repositório.

Para conferir a versão instalada:

```bash
java -version
javac -version
```

## Instalação

```bash
git clone https://github.com/rbkviana/cabenobolso.git
   cd cabenobolso
```

## Organização das pastas

```
.
├── src/
│   ├── Main.java                    # ponto de entrada
│   ├── model/                       # MODEL: regras e dados
│   │   ├── Planejamento.java
│   │   ├── Participante.java
│   │   ├── Despesa.java             # classe abstrata
│   │   ├── DespesaFixa.java
│   │   └── DespesaPorPessoa.java
│   ├── service/                     # validações e cálculos
│   │   ├── ParticipanteService.java
│   │   ├── DespesaService.java
│   │   └── ResumoService.java
│   ├── controller/                  # CONTROLLER
│   │   └── PlanejamentoController.java
│   ├── view/                        # VIEW: telas Swing
│   │   ├── TelaPrincipal.java
│   │   ├── TelaParticipantes.java
│   │   ├── TelaDespesas.java
│   │   └── TelaResumo.java
│   ├── interfaces/
│   │   └── Persistencia.java        # contrato de salvar/carregar
│   └── dao/
│       └── ArquivoPersistencia.java # implementação com serialização
├── dados/
│   └── planejamento.dat             # arquivo de exemplo (caso de referência)
├── docs/
│   ├── diagrama-classes.png         # UML atualizado
│   └── evidencias/                  # capturas de tela dos testes
└── README.md
```

## Compilação

Execute na **raiz do projeto**.

**Linux / macOS / Git Bash:**

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
```

**Windows (Prompt de Comando):**

```bat
mkdir out
dir /s /b src\*.java > fontes.txt
javac -encoding UTF-8 -d out @fontes.txt
```

**Pela IDE:** abra a pasta no IntelliJ IDEA, com o JDK 21 configurado, e rode a classe `Main`.

## Execução

Também na **raiz do projeto**:

```bash
java -cp out Main
```

> ⚠️ **Importante:** o arquivo de dados fica em `dados/planejamento.dat`, um caminho **relativo**. Se o programa for executado de outra pasta, o salvamento e o carregamento não vão encontrar o arquivo certo. Sempre rode a partir da raiz.

## Como usar

1. Na tela principal, clique em **Gerenciar participantes** e cadastre código, nome e orçamento.
2. Clique em **Gerenciar despesas**, informe descrição, valor, tipo (fixa ou por pessoa) e marque os participantes envolvidos.
3. Clique em **Resumo financeiro** para ver os totais e a situação de cada um.
4. Use **Salvar planejamento** para gravar em `dados/planejamento.dat` e **Carregar planejamento** para recuperar.

Entradas inválidas (campo vazio, código repetido, valor negativo, despesa sem participantes) são recusadas com mensagem e **não alteram** os dados existentes.

## Como reproduzir os testes

Os testes são **manuais**. Para cada um, siga a entrada, compare com o resultado esperado e anexe a captura em `docs/evidencias/`.

### Caso de referência (passeio da turma)

Cadastre os dados abaixo.

| Código | Participante | Orçamento |
|---|---|---|
| 1 | Ana | R$ 70,00 |
| 2 | Bruno | R$ 60,00 |
| 3 | Carla | R$ 70,00 |

| Despesa | Tipo | Valor | Participantes |
|---|---|---|---|
| Transporte | Fixa | R$ 90,00 | Ana, Bruno, Carla |
| Almoço | Por pessoa | R$ 20,00 | Ana, Bruno, Carla |
| Atividade recreativa | Fixa | R$ 30,00 | Ana, Carla |
| Sobremesa | Por pessoa | R$ 10,00 | Ana, Bruno, Carla |

**Resultado esperado e obtido (conferem):** custo total R$ 210,00 — Ana R$ 75,00 (excede R$ 5,00), Bruno R$ 60,00 (dentro do limite, total igual ao orçamento), Carla R$ 75,00 (excede R$ 5,00). Evidência: tela *Resumo Financeiro*, seção 4 do relatório.

> Atalho: o arquivo `dados/planejamento.dat` já contém este caso. Basta executar o programa e clicar em **Carregar planejamento**.

### Testes adicionais

As capturas de tela de cada cenário estão na **seção 4 do relatório técnico (PDF)**. Se preferir, copie-as para `docs/evidencias/` com os nomes abaixo.

| # | Cenário | Passos | Resultado esperado | Resultado obtido | Evidência |
|---|---|---|---|---|---|
| 1 | Centavos no rateio | Despesa fixa de R$ 100,00 para os códigos 1, 2 e 3 | R$ 33,34 / R$ 33,33 / R$ 33,33 (soma R$ 100,00) | Ana R$ 33,34, Bruno R$ 33,33, Carla R$ 33,33; total R$ 100,00 ✔ | `docs/evidencias/01-centavos.png` |
| 2 | Entrada ou vínculo inválido | Tentar despesa sem participantes, com valor negativo e com descrição vazia | Mensagem de erro; dados inalterados | Mensagens exibidas: "Selecione pelo menos um participante.", "O valor da despesa deve ser maior que zero." e "A descrição deve ser informada."; nenhuma despesa criada ✔ | `docs/evidencias/02-invalidas.png` |
| 3 | Alteração de orçamento | No caso principal, mudar o orçamento da Ana para R$ 80,00 | Custo continua R$ 75,00, agora dentro do limite | Ana: orçamento R$ 80,00, total R$ 75,00, dentro do orçamento; Carla continua excedendo R$ 5,00 ✔ | `docs/evidencias/03-orcamento.png` |
| 4 | Exclusão de despesa | No caso principal, excluir a atividade recreativa | R$ 60,00 por pessoa; total R$ 180,00 | Total R$ 180,00; Ana, Bruno e Carla com R$ 60,00 cada, todos dentro do orçamento ✔ | `docs/evidencias/04-exclusao.png` |
| 5 | Salvar e reiniciar | Salvar, fechar, abrir e carregar | Todos os dados e vínculos de volta, sem duplicar | Mensagem "Planejamento carregado com sucesso!"; as 4 despesas e seus participantes reaparecem sem duplicação ✔ | `docs/evidencias/05-persistencia.png` |
| 6 | Problema no arquivo | Apagar ou renomear `dados/planejamento.dat` e clicar em Carregar | Mensagem de erro; planejamento em uso preservado; é possível começar vazio | Mensagem "Não foi possível carregar o planejamento: dados\planejamento.dat (O sistema não pode encontrar o arquivo especificado)" ✔ | `docs/evidencias/06-arquivo.png` |

## Modelagem

O diagrama de classes está em [`docs/diagrama-classes.png`](docs/diagrama-classes.png).

- **Abstração, herança e polimorfismo:** `Despesa` é abstrata e define `calcularRateio()`. `DespesaFixa` e `DespesaPorPessoa` sobrescrevem o método, e o restante do sistema usa referências do tipo `Despesa`.
- **Encapsulamento:** atributos privados, listas retornadas de forma imutável por `Planejamento` e `Despesa`.
- **Interface de persistência:** `Persistencia` define `salvar` e `carregar`; `ArquivoPersistencia` implementa com `ObjectOutputStream`/`ObjectInputStream`, e o controlador depende apenas da interface.
- **Coleções:** `ArrayList<Participante>` e `ArrayList<Despesa>` em `Planejamento`, que permitem adicionar, consultar, editar e remover registros mantendo a ordem de cadastro; `Map<Integer, BigDecimal>` no rateio e nos totais, associando o código de cada participante ao seu valor.
- **Precisão:** todos os valores monetários usam `BigDecimal`, evitando erros de ponto flutuante.
- **MVC:** a **view** recebe os dados e mostra resultados; o **controller** encaminha as ações; **model** e **service** validam e calculam.
- **Exceções:** entradas inválidas lançam `IllegalArgumentException` nos serviços; falhas de arquivo lançam `IOException`/`ClassNotFoundException`. Ambas são exibidas na tela com mensagem clara.

## Objetivo extra (simulador de alternativas)

_Não realizado._ 

## Uso de IA e fontes


**Uso de IA:** a equipe utilizou ferramentas de IA (Claude) como apoio em partes do trabalho, como a elaboração do README e a revisão da organização da documentação. As decisões de modelagem, a implementação, a execução dos testes e a análise dos resultados foram conduzidas pela equipe, que revisou e compreendeu o código do projeto.

**Fontes externas:**
- Proposta de atividade da disciplina de Programação Orientada a Objetos, 2026.
- ORACLE. *The Java Tutorials: Creating a GUI With Swing*. https://docs.oracle.com/javase/tutorial/uiswing/
- ORACLE. *BigDecimal (Java SE API Specification)*. https://docs.oracle.com/en/java/javase/26/docs/api/java.base/java/math/BigDecimal.html
