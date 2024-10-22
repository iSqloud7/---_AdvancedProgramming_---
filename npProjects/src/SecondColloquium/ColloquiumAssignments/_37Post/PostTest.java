package SecondColloquium.ColloquiumAssignments._37Post;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Comment extends PostInfo {

    private int likes = 0;

    public Comment(String username, String postContent) {
        super(username, postContent);
        this.likes = 0;
    }

    public void addReply(Comment comment) {
        directComments.add(comment);
    }

    public void like() {
        likes++;
    }

    public int totalLikesOnPost() {
        if (directComments.isEmpty()) {
            return likes;
        } else {
            return likes + directComments.stream()
                    .mapToInt(Comment::totalLikesOnPost).sum();
        }
    }

    /*
       Comment: Time claim water hand career we drug. Purpose size recently reveal usually door majority magazine. As nice real director politics will.
        Written by: user2
        Likes: 10
            Comment: Nice class window call somebody still structure record. Center near general. True spring floor film business data fund. Popular page summer argue present to.
            Written by: user2
            Likes: 6
                Comment: Opportunity radio trouble fill. Media hair rock leave any water practice. Pattern many tax specific light size sign cover.
                Written by: user2
                Likes: 9
                    Comment: White develop side enter human body relationship. Son reality administration professional.
                    Written by: user3
                    Likes: 6
                        Comment: Anything all consider find stage point minute four. Page now more.
                        Written by: user1
                        Likes: 9
    */
    public String printComment(int indent) {
        StringBuilder builder = new StringBuilder();

        String tabs = IntStream.range(0, indent)
                .mapToObj(i -> " ")
                .collect(Collectors.joining(""));

        builder.append(String.format("%sComment: %s", tabs, getPostContent())).append("\n")
                .append(String.format("%sWritten by: %s", tabs, getUsername())).append("\n")
                .append(String.format("%sLikes: %d", tabs, likes)).append("\n");

        directComments.stream()
                .sorted(Comparator.comparing(Comment::totalLikesOnPost)
                        .reversed())
                .forEach(i -> builder.append(i.printComment(indent + 4)));

        return builder.toString();
    }
}

class PostInfo {

    private String username;
    private String postContent;
    List<Comment> directComments;

    public PostInfo(String username, String postContent) {
        this.username = username;
        this.postContent = postContent;
        this.directComments = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPostContent() {
        return postContent;
    }
}

class Post extends PostInfo {

    private Map<String, Comment> commentMap;

    public Post(String username, String postContent) {
        super(username, postContent);
        this.commentMap = new HashMap<>();
    }

    public void addComment(String username, String commentID, String content, String replyToID) {
        Comment comment = new Comment(username, content);
        commentMap.put(commentID, comment);

        if (replyToID != null) {
            Comment parentComment = commentMap.get(replyToID);
            if(parentComment != null){
                parentComment.addReply(comment);
            }
        } else {
            directComments.add(comment);
        }
    }

    public void likeComment(String commentID) {
        commentMap.get(commentID).like();
    }

    /* Format:
       Post: This is a test post for Advanced programming
       Written by: np.finki
       Comments:
          Comment: Time claim water hand career we drug. Purpose size recently reveal usually door majority magazine. As nice real director politics will.
          Written by: user2
          Likes: 10
            Comment: Nice class window call somebody still structure record. Center near general. True spring floor film business data fund. Popular page summer argue present to.
            Written by: user2
            Likes: 6
                Comment: Opportunity radio trouble fill. Media hair rock leave any water practice. Pattern many tax specific light size sign cover.
                Written by: user2
                Likes: 9
                    Comment: White develop side enter human body relationship. Son reality administration professional.
                    Written by: user3
                    Likes: 6
                        Comment: Anything all consider find stage point minute four. Page now more.
                        Written by: user1
                        Likes: 9
    */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("Post: %s", getPostContent())).append("\n")
                .append(String.format("Written by: %s", getUsername())).append("\n")
                .append("Comments: ").append("\n");

        directComments.stream()
                .sorted(Comparator.comparing(Comment::totalLikesOnPost)
                        .reversed())
                .forEach(i -> builder.append(i.printComment(8)));

        return builder.toString();
    }
}

public class PostTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String postAuthor = sc.nextLine();
        String postContent = sc.nextLine();

        Post p = new Post(postAuthor, postContent);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String testCase = parts[0];

            if (testCase.equals("addComment")) {
                String author = parts[1];
                String id = parts[2];
                String content = parts[3];
                String replyToId = null;
                if (parts.length == 5) {
                    replyToId = parts[4];
                }
                p.addComment(author, id, content, replyToId);
            } else if (testCase.equals("likes")) { //likes;1;2;3;4;1;1;1;1;1 example
                for (int i = 1; i < parts.length; i++) {
                    p.likeComment(parts[i]);
                }
            } else {
                System.out.println(p);
            }
        }
    }
}