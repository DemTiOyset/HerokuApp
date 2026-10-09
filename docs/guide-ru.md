# Гайд: запуск HerokuApp_Umarov и сдача через Pull Request

Практическая работа №12. Всё выполняется из корня проекта — папки `HerokuApp`, где лежит `pom.xml`.

Данные этой работы:

| Параметр | Значение |
|---|---|
| GitHub | <https://github.com/DemTiOyset> |
| Репозиторий | `HerokuApp_Umarov` |
| Ветка (как требует задание) | `feature/4ИСП9-42_Umarov_selenium` |
| Ментор-проверяющий | <https://github.com/Tunia666> |

---

## 1. Установка окружения (один раз)

Нужны **JDK 17+** (в проекте используется 21), **Maven 3.9+** и **Google Chrome**.

- **Windows:** JDK — [adoptium.net](https://adoptium.net/temurin/releases/?version=21) (галочка *Set JAVA_HOME*);
  Maven — [maven.apache.org/download.cgi](https://maven.apache.org/download.cgi), папку `bin` добавить в `Path`.
- **Linux:** `sudo apt install -y openjdk-21-jdk maven`
- **macOS:** `brew install openjdk@21 maven`

Проверка:

```bash
java -version    # должно быть 17 или выше
mvn -version     # в строке Java version: та же версия
```

ChromeDriver скачивать не нужно — Selenium Manager подберёт его сам.

---

## 2. Запуск тестов

```bash
mvn clean test
```

Первый запуск дольше (Maven качает зависимости). Успешный результат:

```
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Полезные параметры:

| Команда | Что делает |
|---|---|
| `mvn test -Dheadless=false` | показать окно браузера |
| `mvn test -Dtest=DropdownTest` | только один класс |
| `mvn test -Dtimeout.seconds=60` | увеличить ожидания, если стенд медленный |
| `mvn test -Dpage.load.timeout.seconds=90` | увеличить лимит загрузки страницы |

### 2.1 Отчёты Surefire

После прогона открой:

- `target/surefire-reports/index.html` — сводный отчёт в браузере (имена тестов, статусы, длительности);
- `target/surefire-reports/emailable-report.html` — тот же отчёт одним файлом;
- `target/surefire-reports/*.txt` — краткая сводка по классам;
- `target/surefire-reports/TEST-*.xml` — машинный отчёт, в нём же стектрейсы падений.

Что смотреть при падении: в `index.html` раскрыть упавший тест → в стектрейсе видно строку
`at org.example.herokuapp...` и текст из `Assert` — это место, где ожидание не совпало с фактом.

### 2.2 Скриншоты для отчёта

Код менять не нужно, снимаем терминал:

1. Разверни терминал на весь экран, увеличь шрифт, очисти экран (`cls` или `clear`).
2. Запусти в batch-режиме и сохрани лог в файл:
   - Git Bash / Linux / macOS: `mvn -B clean test 2>&1 | tee test-run.log`
   - PowerShell: `mvn -B clean test 2>&1 | Tee-Object test-run.log`
3. Сделай три скриншота: **команда** `mvn -B clean test`; строки `[check] ...` из тестов;
   итог `Tests run: 15, Failures: 0` + `BUILD SUCCESS`.
4. Отдельно сфотографируй открытый `target/surefire-reports/index.html`.
5. Горячие клавиши: **Windows** `Win + Shift + S`, **macOS** `Cmd + Shift + S`, **Linux** `PrtScn`.

В отчёт по заданию нужны именно эти артефакты: консоль с зелёным прогоном и выдержки из surefire-reports.

---

## 3. Публикация на GitHub

```bash
git config --global user.name "Timur Umarov"
git config --global user.email "твой-email@example.com"

cd путь/до/HerokuApp
git init -b main
git add .
git status          # target/ и .idea/ попасть не должны
git commit -m "feat: add Page Object framework and Add/Remove Elements tests"
```

Дальше создай **пустой** репозиторий на <https://github.com/new>:

- **Name:** `HerokuApp_Umarov`
- **Public** (иначе не будут доступны правила защиты ветки на бесплатном тарифе)
- галочки `Add a README`, `.gitignore`, `license` **не ставить**
- **Create repository**

Свяжи и запушь:

```bash
git remote add origin https://github.com/DemTiOyset/HerokuApp_Umarov.git
git push -u origin main
```

Пароль от аккаунта при пуше не подойдёт — нужен **Personal Access Token**:
<https://github.com/settings/tokens> → *Tokens (classic)* → *Generate new token (classic)* →
галочка `repo` → скопировать `ghp_...` → в терминале вставить его как пароль.

---

## 4. Рабочая ветка и коммиты

Задание требует личную feature-ветку и атомарные коммиты:

```bash
git checkout -b feature/4ИСП9-42_Umarov_selenium
```

Дальше коммить по шагам, а не одним куском — так видно историю работы:

```bash
git add pom.xml .gitignore
git commit -m "chore: init Maven project with Selenium and TestNG"

git add src/test/java/org/example/herokuapp/base src/test/java/org/example/herokuapp/pages/BasePage.java
git commit -m "feat: add TestBase and BasePage for driver lifecycle and explicit waits"

git add src/test/java/org/example/herokuapp/pages
git commit -m "feat: add Page Objects for Add/Remove Elements, Checkboxes, Dropdown, Inputs and Typos"

git add src/test/java/org/example/herokuapp/tests
git commit -m "test: cover five pages with positive and negative scenarios"

git add README.md docs
git commit -m "docs: describe project structure, scenarios and submission steps"

git push -u origin feature/4ИСП9-42_Umarov_selenium
```

После пуша GitHub напечатает в консоли ссылку вида
*Create a pull request for 'feature/4ИСП9-42_Umarov_selenium' on GitHub by visiting: …* — скопируй её.

---

## 5. Pull Request и ментор в Reviewers

1. Открой ссылку из консоли или: репозиторий → **Pull requests** → **New pull request**.
2. Направление: **base: `main`** ← **compare: `feature/4ИСП9-42_Umarov_selenium`**.
3. **Title:** `ЛР12: UI-автотесты HerokuApp (Page Object, Selenium + TestNG)`
4. **Description** (можно скопировать):

```markdown
## Что сделано
- Maven-проект на Java 21, Selenium 4.48 + TestNG 7.10, паттерн Page Object.
- 5 Page Object'ов и 5 тест-классов, 15 тестов (4 негативных).
- Явные ожидания (WebDriverWait), устойчивые локаторы (id, короткие css/xpath).
- Драйвер создаётся в @BeforeMethod и закрывается в @AfterMethod(alwaysRun = true).

## Как проверить
1. mvn clean test (JDK 17+, Maven, Chrome)
2. Ожидается: Tests run: 15, Failures: 0, Errors: 0 — BUILD SUCCESS
3. Отчёт: target/surefire-reports/index.html

## Примечания
- На /typos текст меняется случайно (два допустимых варианта), тест это учитывает.
- На /add_remove_elements/ обработчик addElement() объявлен ниже кнопки — тест ждёт его появления.
- Демо-стенд на Heroku иногда отвечает 10+ секунд, поэтому таймауты увеличены.
```

5. Справа в блоке **Reviewers** нажми шестерёнку и выбери `Tunia666`.
6. **Create pull request**.

> **Главное:** автор PR не может одобрить собственный Pull Request. Approve обязан прийти
> от второго человека — от ментора. Это и есть требуемое «получить ревью».

### 5.1 Где взять ссылку для ментора

Заранее её не существует: адрес появляется только после создания PR. Варианты:

| Ссылка | Где взять |
|---|---|
| `https://github.com/DemTiOyset/HerokuApp_Umarov/pull/1` | адресная строка открытого PR (номер — порядковый) |
| Тот же PR + `/files` | вкладка сразу с кодом и кнопкой **Review changes** |
| `https://github.com/DemTiOyset/HerokuApp_Umarov/pulls` | список PR, если номер забыл |
| `https://github.com/DemTiOyset/HerokuApp_Umarov/invitations` | ссылка для ментора, чтобы принять приглашение |

---

## 6. Приглашение ментора

1. `https://github.com/DemTiOyset/HerokuApp_Umarov/settings/access`
2. **Add people** → `Tunia666` → роль **Write** → **Add**.
   Роль Write нужна, чтобы её Approve засчитало правило защиты ветки.
3. Ментор принимает приглашение из письма (или по ссылке `/invitations`).
4. В PR справа в **Reviewers** выбери `Tunia666` — придёт уведомление «review requested».

Готовый текст сообщения:

```text
Здравствуйте! Сдаю ЛР12 — UI-автотесты для the-internet.herokuapp.com.
Репозиторий: https://github.com/DemTiOyset/HerokuApp_Umarov
Ветка: feature/4ИСП9-42_Umarov_selenium
Pull Request: https://github.com/DemTiOyset/HerokuApp_Umarov/pull/1
Тесты: mvn clean test → Tests run: 15, Failures: 0, BUILD SUCCESS.
Пожалуйста, примите приглашение в коллабораторы и в PR нажмите
Review changes → Approve → Submit review. Спасибо!
```

---

## 7. Защита ветки: без Approve PR не смержить

`Settings` → `Rules` (Rulesets) или `Branches` → новое правило для ветки `main`:

- ✅ **Require a pull request before merging**
- ✅ **Required approvals: 1**
- ✅ **Dismiss stale approvals when new commits are pushed**
- ✅ **Require conversation resolution**
- ✅ **Block force pushes**

Теперь кнопка **Merge pull request** серая с надписью *Review required* до Approve ментора.
Если репозиторий приватный, на бесплатном тарифе правила недоступны — сделай его публичным.

---

## 8. Что делает ментор

> 1. Открыть PR: `https://github.com/DemTiOyset/HerokuApp_Umarov/pull/1`
> 2. Вкладка **Files changed** → посмотреть код.
> 3. **Review changes** → **Approve** → при желании комментарий → **Submit review**.
> 4. Если есть замечания: **Request changes** → студент пушит правки в ту же ветку, PR обновляется.

---

## 9. После Approve

1. **Merge pull request** → **Squash and merge** → **Confirm**.
2. **Delete branch**.
3. Локально:

```bash
git checkout main
git pull
git branch -d feature/4ИСП9-42_Umarov_selenium
```

4. Для отчёта собери:
   - ссылки на ветку и PR;
   - скриншот PR со статусами **Approved** и **Merged**;
   - скриншоты консоли с `mvn clean test` и `target/surefire-reports/index.html`;
   - `hash` последнего коммита: `git log -1 --oneline`.

---

## 10. Типичные проблемы

| Симптом | Причина | Решение |
|---|---|---|
| `java: command not found`, `'mvn' не является внутренней командой` | PATH не видит JDK/Maven | Переустановить, прописать PATH, открыть новый терминал |
| `invalid target release: 21` | Maven работает на старой Java | Поставить JDK 21, проверить `Java version:` в `mvn -version` |
| `Timed out receiving message from renderer` | Демо-стенд «просыпается» после простоя | Повторить запуск, поднять `-Dpage.load.timeout.seconds=90` |
| Тест на `/add_remove_elements/` падает на счётчике элементов | Клик прошёл раньше, чем объявился обработчик `addElement()` | Уже учтено в коде: `BasePage` ждёт `document.readyState`, страница ждёт саму функцию |
| Тест на `/typos` упал на тексте | Сайт внёс опечатку (`you won,t` вместо `you won't`) | Оба варианта уже в списке допустимых в `TyposPage` |
| `Tests run: ... Failures: 1` по таймауту ожидания | Элемент не появился за 30 с | Повторить; при медленном канале поднять `-Dtimeout.seconds=60` |
| `git push` → `Authentication failed` | Использован пароль вместо токена | Создать PAT, вставить его как пароль |
| `remote origin already exists` | remote уже добавлен | `git remote set-url origin https://github.com/DemTiOyset/HerokuApp_Umarov.git` |
| Кнопка Merge серая | Нет Approve ментора | Ментор: Review changes → Approve; проверить роль Write |
| Approve исчез после нового коммита | Включён «Dismiss stale approvals» | Попросить подтвердить PR ещё раз |

---

## 11. Чек-лист сдачи

- [ ] `mvn clean test` → `Tests run: 15, Failures: 0`, `BUILD SUCCESS`
- [ ] Репозиторий `HerokuApp_Umarov` создан и публичный
- [ ] В репозитории нет `target/` и `.idea/`
- [ ] Есть ветка `feature/4ИСП9-42_Umarov_selenium` и атомарные коммиты
- [ ] PR открыт, ментор добавлен в **Reviewers**
- [ ] Ментор добавлен в коллабораторы (роль **Write**) и принял приглашение
- [ ] Включено правило `main`: require PR + required approvals 1
- [ ] На PR есть зелёный статус **Approved**
- [ ] PR смержен (Squash and merge), ветка удалена
- [ ] Собраны скриншоты: консоль `mvn clean test`, `surefire-reports/index.html`, PR с Approved/Merged
- [ ] В отчёте указаны URL ветки, URL PR и hash последнего коммита

---

## 12. Шпаргалка

```bash
mvn clean test
mvn test -Dheadless=false -Dtimeout.seconds=60

git init -b main
git add . && git commit -m "feat: add Page Object framework and tests"
git remote add origin https://github.com/DemTiOyset/HerokuApp_Umarov.git
git push -u origin main

git checkout -b feature/4ИСП9-42_Umarov_selenium
git add . && git commit -m "docs: describe scenarios"
git push -u origin feature/4ИСП9-42_Umarov_selenium

git checkout main && git pull
git branch -d feature/4ИСП9-42_Umarov_selenium
git log -1 --oneline
```
