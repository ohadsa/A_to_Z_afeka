import com.example.integrativit_client.models.ObjectId

data class CommandRequest(
    var targetObject: TargetObject,
    var invokedBy: InvokedBy,
)

data class TargetObject(
    var objectId: ObjectId,
)

data class InvokedBy(
   var userId: UserId,
)
data class UserId(
    val superapp :String = "2023.ohad.saada",
    var email: String,

    )
