[![quality](https://github.com/marciotenorio/PocketTrack/actions/workflows/quality.yml/badge.svg)](https://github.com/marciotenorio/PocketTrack/actions/workflows/quality.yml)
# PocketTrack 

- [Documentação](docs/proposta.md)

## Equipe 
   - **Nome:** Márcio Tenório Júnior
   - **Matrícula:** 20230094909
   - **Papel:** Responsável por todas as etapas do projeto

## Coorte
  - Online

## Sprint 1 — Interface e navegação

- **Vídeo da Sprint 1:** _(adicionar o link)_
- Registro de uso de IA: [docs/uso-de-ia.md](docs/uso-de-ia.md)

### Telas do MVP

| Tela | O que faz |
|---|---|
| Entrar | E-mail e senha, com validação; leva ao cadastro |
| Criar conta | Nome, e-mail, senha e confirmação, com validação |
| Transações | Histórico (mais recentes primeiro) com o resumo do mês no topo |
| Detalhes | Valor, tipo, categoria e data; editar e excluir (com confirmação) |
| Nova / Editar transação | Tipo, descrição, valor, data e categoria; "Salvar" só habilita com tudo válido |
| Resumo mensal | Receitas, despesas, saldo e despesas por categoria, mês a mês |
| Categorias | Cadastro de categorias; só remove as que não estão em uso |
| Ajustes | Conta, tema (sistema, claro ou escuro), lembrete diário e sair |

Os dados ficam em memória nesta sprint (estado elevado acima da navegação). Persistência
local, Firebase e o agendamento do lembrete entram na Sprint 2.

### Navegação

- Rotas tipadas `@Serializable` em [`Rotas.kt`](shared/src/commonMain/kotlin/com/pockettrack/navegacao/Rotas.kt),
  com argumento em `DetalheTransacao(id)` e `EditarTransacao(id)`.
- As telas recebem lambdas; quem navega é o `NavHost`
  ([`GrafoPrincipal.kt`](shared/src/commonMain/kotlin/com/pockettrack/navegacao/GrafoPrincipal.kt)).
- Entrar e Criar conta formam um grafo aninhado
  ([`FluxoAutenticacao.kt`](shared/src/commonMain/kotlin/com/pockettrack/navegacao/FluxoAutenticacao.kt)).

### Deep link

`pockettrack://transacao/{id}` abre o detalhe da transação. Declarado com `navDeepLink` no
grafo e com o `intent-filter` no [`AndroidManifest.xml`](androidApp/src/main/AndroidManifest.xml).

```bash
adb shell am start -a android.intent.action.VIEW -d "pockettrack://transacao/2"
```

Como os dados ainda ficam em memória, o app abre na tela de entrada; depois de entrar, ele
mostra o detalhe da transação 2 (Aluguel), e o Voltar leva à lista de transações.

### Tema e layout adaptativo

- Material 3 com esquema de cor próprio nos modos claro e escuro
  ([`Tema.kt`](shared/src/commonMain/kotlin/com/pockettrack/ui/tema/Tema.kt)); o modo pode ser
  forçado em Ajustes.
- O layout segue a largura da janela (`currentWindowAdaptiveInfo().windowSizeClass`):
  - **compacta** (< 600 dp, celular em pé): barra de navegação inferior;
  - **média** (600–840 dp, celular deitado): trilho de navegação lateral;
  - **expandida** (≥ 840 dp, tablet ou desktop): trilho lateral e, em Transações, lista e
    detalhe lado a lado.
- No desktop a janela abre com tamanho de celular; redimensione para ver as outras larguras.

### Acessibilidade

O que foi feito no código:

- Botões só com ícone têm `contentDescription` (Voltar, Mostrar senha, Escolher data, Mês
  anterior/próximo, Remover categoria). Ícones ao lado de texto são decorativos (`null`).
- Linhas que alternam algo são um único elemento: o lembrete é `toggleable` com papel de
  Switch, e as opções de tipo, tema e horário são `selectable` com papel de RadioButton.
- Títulos de tela e de seção marcados com `heading()`.
- Cada transação da lista é lida como uma frase ("Despesa: Supermercado, R$ 387,90,
  Alimentação, 06/10/2026") e anuncia se está selecionada no modo lista e detalhe.
- Campos com erro anunciam o erro (`semantics { error(...) }`); o nome do mês no resumo é uma
  *live region*.
- Cores só pelos papéis do tema; alvos de toque de pelo menos 48 dp (componentes Material 3
  e linhas com altura mínima de 56 dp).

Verificação no aparelho (TalkBack e Accessibility Scanner): _(registrar aqui o que foi
encontrado e corrigido)_

### Qualidade

O workflow [`quality.yml`](.github/workflows/quality.yml) compila Android, desktop e iOS e
roda `ktlint`, `detekt` e os testes. As regras das telas (validação, dinheiro, datas e resumo)
são funções puras com testes em
[`RegrasTest.kt`](shared/src/commonTest/kotlin/com/pockettrack/RegrasTest.kt).

## Description

This is a Kotlin Multiplatform project targeting Android, iOS, Desktop (JVM).

* [/iosApp](./iosApp/iosApp) contains an iOS application. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/shared](./shared/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./shared/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./shared/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./shared/src/jvmMain/kotlin)
    folder is the appropriate location.

### Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `./gradlew :androidApp:assembleDebug`
- Desktop app:
  - Hot reload: `./gradlew :desktopApp:hotRun --auto`
  - Standard run: `./gradlew :desktopApp:run`
- iOS app: open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- Android tests: `./gradlew :shared:testAndroidHostTest`
- Desktop tests: `./gradlew :shared:jvmTest`
- iOS tests: `./gradlew :shared:iosSimulatorArm64Test`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
