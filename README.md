# Очень хочу получить фидбек по архитектуре. 
За время выполнения домашки перечитал все статьи по MVVM, перехожу на нее с Java + MVP. Подход мне нравится, но я совсем не понимаю как организовать работу. Есть пара вопросов.

1. Как синхронизировать два фрагмента в двух разных активити? Грубо говоря, в активити создания во фрагменте создания мы добавляем таску, а в onResume фрагмента со списком тасок, который в меин активити мы обновляем список тасок. Я сделал вот как: У меня есть CreateFragment, создав новую таску я кидаю ее в ProblemModel (синглтончик), а в onResume ProblemsFragment со списком этих тасок я звоню во вью модель, где получаю заного данные из модели. Вроде получается что-то вроде вью-вьюмодель-модель, но как мне кажется, слишком много действий плюс дополнительно нужно синхронить удаление таски с этим синглтоном. 
2. С одним фрагментом у меня все просто, я сабаюсь на события списка во вьюмодели и все. Но как добавить в этот список новую таску из другого активити? Можно ли как-то связать все это дело без моего костыля? 
