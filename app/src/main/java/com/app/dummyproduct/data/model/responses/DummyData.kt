package com.app.dummyproduct.data.model.responses

data class DummyData(
    var name : String,
    var image: Int,
    var likes: String,
    var duration: String,
    var isClick: Boolean,
    var nameTwo: String,
) {
    override fun toString(): String {
        return "$name"
    }
}