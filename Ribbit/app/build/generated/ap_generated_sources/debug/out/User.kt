data class User(
    val backgroundImage: String,
    val bio: String,
    val birthDate: String,
    val email: String,
    val followed: Boolean,
    val followers: List<String>,
    val followings: List<String>,
    val fullName: String,
    val id: Int,
    val image: String,
    val location: String,
    val login_with_google: Boolean,
    val mobile: String,
    val req_user: Boolean,
    val verified: Boolean,
    val website: String
)