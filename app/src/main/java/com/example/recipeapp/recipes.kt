package com.example.recipeapp

object STUB {
    private val categories: List<Category> = listOf(
        Category(
            0,
            "Бургеры",
            "Рецепты всех популярных видов бургеров",
            "app/src/main/assets/burger.png"
        ),
        Category(
            1,
            "Десерты",
            "Самые вкусные рецепты десертов специально для вас",
            "app/src/main/assets/dessert.png"
        ),
        Category(
            2,
            "Пицца",
            "Пицца на любой вкус и цвет. Лучшая подборка для тебя",
            "app/src/main/assets/pizza.png"
        ),
        Category(
            3,
            "Рыба",
            "Печеная, жареная, сушеная, любая рыба на твой вкус",
            "app/src/main/assets/fish.png"
        ),
        Category(
            4,
            "Супы",
            "От классики до экзотики: мир в одной тарелке",
            "app/src/main/assets/soup.png"
        ),
        Category(
            5,
            "Салаты",
            "Хрустящий калейдоскоп под соусом вдохновения",
            "app/src/main/assets/salad.png"
        ),
    )

    fun getCategories(): List<Category> {
        return categories
    }
}