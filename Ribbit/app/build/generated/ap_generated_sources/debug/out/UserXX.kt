
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserXX(
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
    @SerialName("login_with_google")
    val loginWithGoogle: Boolean,
    val mobile: String,
    @SerialName("req_user")
    val reqUser: Boolean,
    val verified: Boolean,
    val website: String
)