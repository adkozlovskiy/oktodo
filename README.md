# Affari Plans
Основные задачи, которые вам нужно сделать в рамках этого этапа:
- Продумать и описать модели данных, с которыми вы будете работать в рамках приложения. Нужно учесть, что у каждой задачи есть текстовое описание (неограниченное по длине), дедлайн (дата+время или же только дата), важность. Также вам могут потребоваться разные технические параметры.
- Реализацию хранения этих данных можно пока не делать. Для отладки и написания UI-части можно подготовить N разных замоканных данных в коде приложения + хранить новые задачи в in-memory кэше (чтобы проверять добавление новых задач).
- Сверстать экран списка задач. Сортировка происходит сверху вниз, по ближайшей дате / времени и по срочности. То есть с самого верха списка должна быть ближайшая по дедлайну и самая важная задача.
- Нужно иметь возможность показать выполненные задачи, по умолчанию они должны быть скрыты.
- Список можно скроллить до конца. Шапка "мои дела" должна закрепляться как на примере с дизайна. Тап по ней выполняет отскролл до верха списка.
- Каждый элемент в списке должен содержать текст задачи (максимум 3 строки), отображать приоритет и чекбокс сделанности. Если задача просрочена, чекбокс подсвечивается красным цветом. Каждый элемент списка можно свайпнуть влево для удаления и вправо для переключения флага "сделанности".
- Элементы, отмеченные как выполненные, не должны сразу скрываться, а только при перезаходе на главный экран.
- Нужно добавить кнопку создания новой задача, которая ведет на отдельный экран. Нужно реализовать этот экран, чтобы пользователь мог заполнить все требуемые данные по задаче. Пример можно посмотреть в дизайне.
- Приложение должно корректно работать в landscape режиме.
- Если у пользователя есть задачи на сегодня, нужно утром показать ему уведомление с текстом вида "Сегодня вам нужно сделать X задач, не забудьте". Клик по уведомлению ведет в приложение.

Бонусная часть: добавьте в приложение поддержку темной темы.
