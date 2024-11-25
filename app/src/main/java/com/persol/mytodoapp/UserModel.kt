package com.persol.mytodoapp

data class UserModel(
    val name: String,
    val image: String,
    val userID: Long,
) {
    companion object {
        private val postMap = mapOf(
            1 to UserModel("Mark", "https://randomuser.me/api/portraits/men/62.jpg",
                1
            ),
            2 to UserModel("John", "https://randomuser.me/api/portraits/men/28.jpg",
                2
            ),
            3 to UserModel("Jane", "https://randomuser.me/api/portraits/women/1.jpg",
                3
            ),
            4 to UserModel("Dom", "https://randomuser.me/api/portraits/men/32.jpg",
                4
            ),
            5 to UserModel("Jarred", "https://randomuser.me/api/portraits/men/12.jpg",
                5
            ),
            6 to UserModel("Paul", "https://randomuser.me/api/portraits/men/13.jpg",
                6
            ),
            7 to UserModel("Ken", "https://randomuser.me/api/portraits/men/46.jpg",
            7
            ),
            8 to UserModel("Mia", "https://randomuser.me/api/portraits/women/50.jpg",
                8
            ),
            9 to UserModel("Jade", "https://randomuser.me/api/portraits/women/77.jpg",
                9
            ),
            10 to UserModel("Karen", "https://randomuser.me/api/portraits/women/58.jpg",
                10
            )
        )
        fun getUser(userID: Int): UserModel {
            return postMap.getValue(userID)
        }
    }
}
