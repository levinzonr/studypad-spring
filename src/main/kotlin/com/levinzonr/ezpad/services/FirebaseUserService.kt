package com.levinzonr.ezpad.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import com.google.gson.Gson
import com.levinzonr.ezpad.domain.errors.BadRequestException
import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.UserRepository
import com.levinzonr.ezpad.utils.fromJson
import com.levinzonr.ezpad.utils.tryGet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FirebaseUserService : UserService {


    @Autowired
    private lateinit var auth: FirebaseAuth

    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var universityService: UniversityService

    @Autowired
    private lateinit var notebookService: NotebookService

    @Autowired
    private lateinit var notesService: NotesService

    data class ExportedNotebook(val name: String, val notes: Map<String, ExportedNote>)


    data class ExportedNote(val title: String, val content: String)

    private val firstNotebookString = "{\n" +
            "    \"colorCode\" : \"FF0000\",\n" +
            "    \"importedId\" : \"-KvwN-NgcWmGQo0vFy-J\",\n" +
            "    \"name\" : \"Linear Algebra\",\n" +
            "    \"notes\" : {\n" +
            "      \"-LBLKWKCKerIgDEumrU9\" : {\n" +
            "        \"content\" : \"Let A ∈ F m,n . By the term rank of matrix A we understand the dimension of linear span of rows of matrix A (as vectors from F1,n ) and denote it h(A). That means h(A) = dimhA1:, . . . , Am:i.\",\n" +
            "        \"firebaseKey\" : \"-LBLKWKCKerIgDEumrU9\",\n" +
            "        \"id\" : 0,\n" +
            "        \"timesAnswered\" : 0,\n" +
            "        \"timesHintRevealed\" : 0,\n" +
            "        \"timesSkipped\" : 0,\n" +
            "        \"type\" : \"Matrix Rank\"\n" +
            "      },\n" +
            "      \"-LBLKWKCKerIgDEumrUA\" : {\n" +
            "        \"content\" : \"The matrix A ∈ F n,n is called regular, if there exists matrix B ∈ F n,n such that AB = BA = I. The matrix B is then called the inverse matrix to matrix A, denoted B = A −1 . Matrix A, which is not regular, is called singular.\",\n" +
            "        \"firebaseKey\" : \"-LBLKWKCKerIgDEumrUA\",\n" +
            "        \"id\" : 0,\n" +
            "        \"timesAnswered\" : 0,\n" +
            "        \"timesHintRevealed\" : 0,\n" +
            "        \"timesSkipped\" : 0,\n" +
            "        \"type\" : \"Regular Matrix\"\n" +
            "      },\n" +
            "      \"-LBLKWKD7yAtkO5rfKVb\" : {\n" +
            "        \"content\" : \"Let A ∈ F m,n .\\n\\n (i) System Ax = b of m linear equations in n unknown variables is solvable, i.e., S 6= ∅, if and only if h(A) = h(A | b). \\n\\n(ii) If h(A) = h, then the set of all solutions of the homogeneous equation Ax = θ is a subspace with dimension n − h, i.e., there exist a LI sequence (z1, . . . , zn−h) of vectors from Fn,1 such that S0 = ( {θ}, for n = h, hz1, . . . , zn−hi, for h < n.\\n\\n If further h(A | b) = h, then S = ˜x + S0, where x˜ is particular solution of Ax˜ = b.\",\n" +
            "        \"firebaseKey\" : \"-LBLKWKD7yAtkO5rfKVb\",\n" +
            "        \"id\" : 0,\n" +
            "        \"timesAnswered\" : 0,\n" +
            "        \"timesHintRevealed\" : 0,\n" +
            "        \"timesSkipped\" : 0,\n" +
            "        \"type\" : \"Frobenius Theorem\"\n" +
            "      },\n" +
            "      \"-LBLKWKEBVBUiCUSyaIw\" : {\n" +
            "        \"content\" : \"Coding is any mapping κ : X → A+\",\n" +
            "        \"firebaseKey\" : \"-LBLKWKEBVBUiCUSyaIw\",\n" +
            "        \"id\" : 0,\n" +
            "        \"timesAnswered\" : 0,\n" +
            "        \"timesHintRevealed\" : 0,\n" +
            "        \"timesSkipped\" : 0,\n" +
            "        \"type\" : \"Coding\"\n" +
            "      },\n" +
            "      \"-LBLKWKEBVBUiCUSyaIx\" : {\n" +
            "        \"content\" : \"Code is the range of the coding κ, i.e., image of source alphabet C = Ran(κ) = C(X )\",\n" +
            "        \"firebaseKey\" : \"-LBLKWKEBVBUiCUSyaIx\",\n" +
            "        \"id\" : 0,\n" +
            "        \"timesAnswered\" : 0,\n" +
            "        \"timesHintRevealed\" : 0,\n" +
            "        \"timesSkipped\" : 0,\n" +
            "        \"type\" : \"Code\"\n" +
            "      },\n" +
            "      \"-LBLKWKEBVBUiCUSyaIy\" : {\n" +
            "        \"content\" : \"\\\"For two words u, v ∈ An we define the Hamming distance of u = u1u2 . . . un and v = v1v2 . . . vn as the number of indexes at which u and v differ, i.e., d(u, v) := \\f \\f \\b j ∈ {1, . . . , n} | uj 6= vj \\t\\\"\",\n" +
            "        \"firebaseKey\" : \"-LBLKWKEBVBUiCUSyaIy\",\n" +
            "        \"id\" : 0,\n" +
            "        \"timesAnswered\" : 0,\n" +
            "        \"timesHintRevealed\" : 0,\n" +
            "        \"timesSkipped\" : 0,\n" +
            "        \"type\" : \"Hamming Distance\"\n" +
            "      },\n" +
            "      \"-LBLKWKEBVBUiCUSyaIz\" : {\n" +
            "        \"content\" : \"Let A is finite field. The code C ⊂ A+ is called linear (n,k)-code, if C is a subspace of An with dimension k (C ⊂⊂ An , dim C = k) Matrix G ∈ Ak,n consisting of arbitrary basis of C written in rows, is called generating matrix of C. Matrix H ∈ An−k,n such that C is the solution set of SLE Hx = θ, is called check matrix of C.\",\n" +
            "        \"firebaseKey\" : \"-LBLKWKEBVBUiCUSyaIz\",\n" +
            "        \"id\" : 0,\n" +
            "        \"timesAnswered\" : 0,\n" +
            "        \"timesHintRevealed\" : 0,\n" +
            "        \"timesSkipped\" : 0,\n" +
            "        \"type\" : \"Linear code\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n"


    override fun createUser(email: String, password: String, firstName: String?, lastName: String?): User {
        val displayName = if ("${firstName ?: ""} ${lastName
                        ?: ""}".isBlank()) "Unknown user" else "$firstName $lastName"
        println("Create from firebase reqyest")

        val existedUser : UserRecord? = try { auth.getUserByEmail(email) } catch (e: Exception) { null }
        if (existedUser != null) {
            throw BadRequestException("User already exist")
        }


        val request = UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(displayName)
                .setEmailVerified(false)



        val userRecord = auth.createUserAsync(request).get()

        val user = User(userRecord.uid, userRecord.email, firstName, lastName, userRecord.displayName, userRecord.photoUrl)
        return repository.save(user)
    }

    override fun createUser(firebaseId: String): User {
        val userRecord = auth.getUser(firebaseId)
        println("Create from firebase recoed")
        val names = userRecord.displayName.split(" ")
        val firstName = names.getOrNull(0) ?: ""
        val lastName = names.getOrNull(1) ?: ""

        val nb = Gson().fromJson<ExportedNotebook>(firstNotebookString)


        val dbUser = User(userRecord.uid, userRecord.email, firstName, lastName, userRecord.displayName, userRecord.photoUrl)
        return repository.save(dbUser).apply { isNewUser = true }.also {

            val created = notebookService.createNewNotebook(nb.name, it)
            nb.notes.entries.map { it.value }.forEach { notesService.createNote(it.title, it.content, created) }
        }
    }

    override fun findUserById(id: String): User? {
        val user = repository.findById(id).tryGet()
        println("Found user: ${user?.id}")
        return user
    }

    override fun registerFirebaseToken(userId: String, token: String) {
        val user = repository.findById(userId).orElse(null) ?: return
        if (user.firebaseTokens.contains(token))  return
        val tokens = user.firebaseTokens.toMutableList().apply { add(token) }
        val updated = user.copy(firebaseTokens = tokens)
        repository.save(updated)
    }

    override fun updateUser(userId: String, displayName: String?, universityId: Long?) : User {
        var user = findUserById(userId) ?: throw NotFoundException.Builder(User::class).buildWithId(userId)
        universityId?.let { id ->
            val uni = universityService.findById(id)
            user = repository.save(user.copy(university = uni))
        }
        val name = displayName ?: user.displayName
        user = repository.save(user.copy(displayName = name))

        return user
    }
}