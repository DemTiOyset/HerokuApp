Change # HerokuApp_Umarov

UI-автотесты для учебного стенда **The Internet** (<https://the-internet.herokuapp.com>) —
практическая работа №12 по дисциплине «Тестирование информационных систем».

Проект построен по паттерну **Page Object**: локаторы и действия со страницей живут в классах
`pages/*`, сами проверки — в `tests/*`, а жизненный цикл браузера — в базовом классе `base/TestBase`.

## Стек

| Компонент | Версия |
|---|---|
| Java | 21 (требуется 17+) |
| Maven | 3.9+ |
| Selenium WebDriver | 4.48.0 |
| TestNG | 7.10.2 |
| Браузер | Google Chrome (headless по умолчанию) |

Драйвер отдельно скачивать не нужно: Selenium 4 подбирает и при необходимости загружает
ChromeDriver через встроенный **Selenium Manager** (поэтому WebDriverManager не подключён).

## Структура проекта

```
HerokuApp/
├── pom.xml
├── README.md
├── docs/
│   └── guide-ru.md                                    # гайд: запуск, GitHub, ветка, PR, проверка ментором
└── src/test/java/org/example/herokuapp/
    ├── base/
    │   └── TestBase.java                              # WebDriver, WebDriverWait, @BeforeMethod/@AfterMethod(alwaysRun = true)
    ├── pages/
    │   ├── BasePage.java                              # базовый URL, ожидание готовности страницы
    │   ├── AddRemoveElementsPage.java
    │   ├── CheckboxesPage.java
    │   ├── DropdownPage.java
    │   ├── InputsPage.java
    │   └── TyposPage.java
    └── tests/
        ├── AddRemoveElementsTest.java
        ├── CheckboxesTest.java
        ├── DropdownTest.java
        ├── InputsTest.java
        └── TyposTest.java
```

## Реализованные сценарии

Всего 15 тестов в 5 классах, из них 4 негативных.

### AddRemoveElementsTest — `/add_remove_elements/`
| Тест | Проверка |
|---|---|
| `shouldAddTwoElements` | два нажатия «Add Element» дают ровно 2 элемента |
| `shouldDeleteOneOfTwoElements` | после удаления одного из двух остаётся 1 |
| `shouldNotDeleteAnythingOnUntouchedPage` *(негативный)* | на нетронутой странице кнопки «Delete» нет, счётчик равен 0 |

### CheckboxesTest — `/checkboxes`
| Тест | Проверка |
|---|---|
| `shouldCheckFirstCheckbox` | первый чекбокс изначально снят и становится отмеченным |
| `shouldUncheckSecondCheckbox` | второй чекбокс изначально отмечен и снимается |
| `shouldNotAffectSecondCheckboxWhenFirstIsToggled` *(негативный)* | переключение первого не меняет второй |

### DropdownTest — `/dropdown`
| Тест | Проверка |
|---|---|
| `shouldContainAllExpectedOptions` | в списке есть плейсхолдер и обе опции |
| `shouldSelectFirstOption` | выбирается «Option 1» |
| `shouldSelectSecondOption` | выбирается «Option 2» |
| `shouldNotAllowSelectingDisabledPlaceholder` *(негативный)* | плейсхолдер `disabled` и не остаётся выбранным |

### InputsTest — `/inputs`
| Тест | Проверка |
|---|---|
| `shouldIncrementValueWithArrowUp` | `10` → `ARROW_UP` → `11` |
| `shouldDecrementValueWithArrowDown` | `10` → `ARROW_DOWN` → `9` |
| `shouldRejectNonNumericInput` *(негативный)* | буквы в числовое поле не вводятся |

### TyposTest — `/typos`
| Тест | Проверка |
|---|---|
| `shouldMatchOneOfAllowedParagraphVariants` | абзац совпадает с одним из двух допустимых вариантов |
| `shouldContainStableIntroduction` | первое предложение всегда одинаковое |

## Быстрый запуск

```bash
# из корня проекта (папка HerokuApp)
mvn clean test
```

Полезные варианты:

```bash
mvn test -Dheadless=false               # показать окно браузера
mvn test -Dtimeout.seconds=60           # увеличить ожидания
mvn test -Dpage.load.timeout.seconds=90 # увеличить лимит загрузки страницы
mvn test -Dtest=DropdownTest            # один класс
mvn test -Dtest='*Checkbox*'            # по маске
```

Отчёты Surefire: `target/surefire-reports/` — `index.html` (открыть в браузере),
`emailable-report.html`, `*.txt` и `TEST-*.xml`.
Больше подробностей — в разборе в [docs/guide-ru.md](docs/guide-ru.md).

## Особенности стенда, которые учтены в коде

1. **Typos: опечатка вносится случайно.** Проверено 40 загрузками: у сайта ровно два варианта
   текста, различаются одним символом — `you won't.` и `you won,t.`. Поэтому тест сравнивает
   абзац со списком допустимых вариантов, а не с одной строкой.
2. **Handler может появиться позже кнопки.** На `/add_remove_elements/` функция `addElement()`
   объявлена в `<script>` **ниже** самой кнопки. Кнопка становится кликабельной раньше, чем
   существует обработчик, и клик в этот момент молча ничего не делает. `BasePage.open()` ждёт
   `document.readyState === 'complete'`, а страница дополнительно ждёт появления самой функции.
3. **Стенд периодически «просыпается».** Heroku-демо иногда отвечает 10+ секунд, поэтому лимит
   загрузки страницы (60 с) выше таймаута явных ожиданий (30 с).
4. **Hovers: ссылки профилей отдают 404** (наблюдение, в объём работы не вошло). Проверено:
   `/users/1`, `/users/2`, `/users/3` возвращают `404 Not Found`, хотя на странице `/hovers`
   это ссылки на профили. То есть клик по профилю на этом стенде ведёт на несуществующую
   страницу — это дефект демо-стенда, а не ошибка теста.

## Ограничения

1. Нужен интернет: тесты работают против публичного демо-стенда, а не локальной сборки.
2. Скорость прогона зависит от стенда: если Heroku «просыпается», серия может идти несколько минут
   (наблюдались прогоны по 8 минут при загрузке страниц по 10 секунд).
3. **На медленном стенде возможен единичный флак.** Проверки на `/add_remove_elements/` иногда
   теряют клик: стенд под нагрузкой добавляет элемент, но не успевает обработать удаление
   (в двух полных прогонах из трёх проходили 14 тестов из 15, причём падал каждый раз разный).
   Лечится повторным `mvn test` — либо запуском классов по отдельности:
   `mvn test -Dtest=AddRemoveElementsTest`. Логика теста при этом корректна, ожидания явные.
4. Проверяется только Chrome; кросс-браузерность и CI в объём этой работы не входили
   (в задании они отмечены как опциональные).

## Автор

Умаров Тимур, группа 4ИСП9-42 — практическая работа №12.
