package com.levinzonr.ezpad.controllers

import com.google.gson.Gson
import com.levinzonr.ezpad.domain.model.Topic
import com.levinzonr.ezpad.domain.payload.TopicPayload
import com.levinzonr.ezpad.domain.payload.UserFeedbackPayload
import com.levinzonr.ezpad.security.StudyPadUserDetails
import com.levinzonr.ezpad.services.*
import org.apache.http.HttpException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/config")
class ConfigurationController {


    @Autowired
    private lateinit var topicService: TopicService

    @Autowired
    private lateinit var tagService: TagService

    @Autowired
    private lateinit var feedbackService: FeedbackService

    @Autowired
    private lateinit var userService: UserService

    @Autowired private lateinit var notebokService: NotebookService

    @Autowired
    private lateinit var noteService: NotesService



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


    @PostMapping("/topics")
    fun createTopic(@RequestBody topicPayload: TopicPayload) : Topic {
        return topicService.createTopic(topicPayload.name)
    }

    @PostMapping("/topics/all")
    fun createTopics(@AuthenticationPrincipal userDetails: StudyPadUserDetails, @RequestBody topicPayload: List<String>) {
        if (userService.hasAdminPriveleges(userDetails.userId)) {
            topicPayload.forEach {
                topicService.createTopic(it)
            }
        }
    }

    @PostMapping("/tags/all")
    fun createTags(@RequestBody tags: List<String>) {
        tags.forEach { tagService.createTag(it) }
    }

    @DeleteMapping("/topics/{id}")
    fun deleteTopic(@AuthenticationPrincipal userDetails: StudyPadUserDetails, @PathVariable("id") id: Long) {
        if (userService.hasAdminPriveleges(userDetails.userId)) {
            return topicService.deleteTopic(id)
        }
    }

    @PatchMapping("/topics/{id}")
    fun updateTopic(@AuthenticationPrincipal userDetails: StudyPadUserDetails, @PathVariable("id") id: Long, @RequestBody topicPayload: TopicPayload) : Topic {
        if (userService.hasAdminPriveleges(userDetails.userId)) {
        return topicService.editTopic(id, topicPayload.name)
        } else {
            throw HttpException()
        }
    }

    @PostMapping("/notebooks/all")
    fun postExportedNotebooks(@AuthenticationPrincipal userDetails: StudyPadUserDetails, @RequestBody notebooks: Map<String, ExportedNotebook>) {
        if (userService.hasAdminPriveleges(userDetails.userId))
        notebooks.forEach { notebooks ->
            val user = userService.findUserById(userDetails.userId) ?: throw Exception()
            notebokService.createNewNotebook(notebooks.value.name,  user, true).also { nb ->
                notebooks.value.notes.forEach {
                    noteService.createNote(it.value.title, it.value.content, nb)
                }
            }
        }
    }

    @GetMapping("/topics")
    fun getTopics() : List<Topic> {
        return topicService.getTopics()
    }


    @PostMapping("/feedback")
    fun postUserFeedback(@AuthenticationPrincipal userDetails: StudyPadUserDetails, @RequestBody payload: UserFeedbackPayload) {
        feedbackService.saveFeedback(userDetails.userId, payload)
    }


}