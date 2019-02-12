package com.levinzonr.ezpad.services

import com.google.gson.Gson
import com.levinzonr.ezpad.domain.ApiMessages
import com.levinzonr.ezpad.domain.errors.BadRequestException
import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.model.UserRole
import com.levinzonr.ezpad.domain.payload.FacebookUser
import com.levinzonr.ezpad.domain.repositories.UserRepository
import com.levinzonr.ezpad.utils.fromJson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var universityService: UniversityService


    @Autowired
    private lateinit var notebookService: NotebookService

    @Autowired
    private lateinit var notesService: NotesService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

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
            "        \"title\" : \"Matrix Rank\"\n" +
            "      },\n" +
            "      \"-LBLKWKCKerIgDEumrUA\" : {\n" +
            "        \"content\" : \"The matrix A ∈ F n,n is called regular, if there exists matrix B ∈ F n,n such that AB = BA = I. The matrix B is then called the inverse matrix to matrix A, denoted B = A −1 . Matrix A, which is not regular, is called singular.\",\n" +
            "        \"firebaseKey\" : \"-LBLKWKCKerIgDEumrUA\",\n" +
            "        \"id\" : 0,\n" +
            "        \"timesAnswered\" : 0,\n" +
            "        \"timesHintRevealed\" : 0,\n" +
            "        \"timesSkipped\" : 0,\n" +
            "        \"title\" : \"Regular Matrix\"\n" +
            "      },\n" +
            "      \"-LBLKWKD7yAtkO5rfKVb\" : {\n" +
            "        \"content\" : \"Let A ∈ F m,n .\\n\\n (i) System Ax = b of m linear equations in n unknown variables is solvable, i.e., S 6= ∅, if and only if h(A) = h(A | b). \\n\\n(ii) If h(A) = h, then the set of all solutions of the homogeneous equation Ax = θ is a subspace with dimension n − h, i.e., there exist a LI sequence (z1, . . . , zn−h) of vectors from Fn,1 such that S0 = ( {θ}, for n = h, hz1, . . . , zn−hi, for h < n.\\n\\n If further h(A | b) = h, then S = ˜x + S0, where x˜ is particular solution of Ax˜ = b.\",\n" +
            "        \"firebaseKey\" : \"-LBLKWKD7yAtkO5rfKVb\",\n" +
            "        \"id\" : 0,\n" +
            "        \"timesAnswered\" : 0,\n" +
            "        \"timesHintRevealed\" : 0,\n" +
            "        \"timesSkipped\" : 0,\n" +
            "        \"title\" : \"Frobenius Theorem\"\n" +
            "      },\n" +
            "      \"-LBLKWKEBVBUiCUSyaIw\" : {\n" +
            "        \"content\" : \"Coding is any mapping κ : X → A+\",\n" +
            "        \"firebaseKey\" : \"-LBLKWKEBVBUiCUSyaIw\",\n" +
            "        \"id\" : 0,\n" +
            "        \"timesAnswered\" : 0,\n" +
            "        \"timesHintRevealed\" : 0,\n" +
            "        \"timesSkipped\" : 0,\n" +
            "        \"title\" : \"Coding\"\n" +
            "      },\n" +
            "      \"-LBLKWKEBVBUiCUSyaIx\" : {\n" +
            "        \"content\" : \"Code is the range of the coding κ, i.e., image of source alphabet C = Ran(κ) = C(X )\",\n" +
            "        \"firebaseKey\" : \"-LBLKWKEBVBUiCUSyaIx\",\n" +
            "        \"id\" : 0,\n" +
            "        \"timesAnswered\" : 0,\n" +
            "        \"timesHintRevealed\" : 0,\n" +
            "        \"timesSkipped\" : 0,\n" +
            "        \"title\" : \"Code\"\n" +
            "      },\n" +
            "      \"-LBLKWKEBVBUiCUSyaIy\" : {\n" +
            "        \"content\" : \"\\\"For two words u, v ∈ An we define the Hamming distance of u = u1u2 . . . un and v = v1v2 . . . vn as the number of indexes at which u and v differ, i.e., d(u, v) := \\f \\f \\b j ∈ {1, . . . , n} | uj 6= vj \\t\\\"\",\n" +
            "        \"firebaseKey\" : \"-LBLKWKEBVBUiCUSyaIy\",\n" +
            "        \"id\" : 0,\n" +
            "        \"timesAnswered\" : 0,\n" +
            "        \"timesHintRevealed\" : 0,\n" +
            "        \"timesSkipped\" : 0,\n" +
            "        \"title\" : \"Hamming Distance\"\n" +
            "      },\n" +
            "      \"-LBLKWKEBVBUiCUSyaIz\" : {\n" +
            "        \"content\" : \"Let A is finite field. The code C ⊂ A+ is called linear (n,k)-code, if C is a subspace of An with dimension k (C ⊂⊂ An , dim C = k) Matrix G ∈ Ak,n consisting of arbitrary basis of C written in rows, is called generating matrix of C. Matrix H ∈ An−k,n such that C is the solution set of SLE Hx = θ, is called check matrix of C.\",\n" +
            "        \"firebaseKey\" : \"-LBLKWKEBVBUiCUSyaIz\",\n" +
            "        \"id\" : 0,\n" +
            "        \"timesAnswered\" : 0,\n" +
            "        \"timesHintRevealed\" : 0,\n" +
            "        \"timesSkipped\" : 0,\n" +
            "        \"title\" : \"Linear code\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n"

    override fun createUser(email: String, password: String,
                            firstName: String?, lastName: String?, photoUrl: String?, role: UserRole): User {

        userRepository.findByEmail(email)?.let {
            throw BadRequestException(ApiMessages.ErrorMessages.ERROR_USER_EXISTS)
        }

        val nb = Gson().fromJson<ExportedNotebook>(firstNotebookString)


        val displayName = "${firstName ?: ""} ${lastName ?: ""}"

        val user = User(
                email = email,
                password = passwordEncoder.encode(password),
                firstName = firstName,
                lastName = lastName,
                displayName = if (firstName == null && lastName == null) "Unknown user" else displayName,
                photoUrl = photoUrl,
                roles = setOf(role)
        )

        return userRepository.save(user).apply { isNewUser = true }.also {

            val created = notebookService.createNewNotebook(nb.name, it)
            nb.notes.entries.map { it.value }.forEach { notesService.createNote(it.title, it.content, created) }
        }
    }

    override fun getUserById(id: Long): User {
        return userRepository.findById(id)
                .orElseThrow {
                    NotFoundException.Builder(User::class)
                            .buildWithId(id.toString())
                }
    }

    override fun updateUserById(uuid: Long, firstName: String?, lastName: String?, password: String?): User {
        val user = getUserById(uuid)
        val updated = user.copy(
                firstName = firstName ?: user.firstName,
                lastName = lastName ?: user.lastName,
                displayName = "$firstName $lastName",
                password = password
        )
        return userRepository.save(updated)
    }

    // TODO BBetter password handling
    override fun processFacebookUser(facebookUser: FacebookUser): User {
        // Facebook User doesn't exist
        return userRepository.findByEmail(facebookUser.email!!) ?:
        createUser(facebookUser.email, facebookUser.id!!, facebookUser.first_name, facebookUser.last_name, null, UserRole.FACEBOOK_USER)
    }

    override fun getUserEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw NotFoundException.Builder(User::class).buildWithId(email)
    }

    override fun updateUserUniversity(userId: Long, universityId: Long): User {
        println(universityService.findAll())
        val uni =  universityService.findById(universityId)
        val newUser = getUserById(userId).copy(university = uni)
        userRepository.save(newUser)
        return newUser
    }
}