package FirstColloquium.ColloquiumAssignments._23Quiz;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class InvalidOperationException extends Exception {

    public InvalidOperationException(String answer) {
        super(answer);
    }
}

class TrueAndFalseQuestion extends Question {

    private boolean answer;

    public TrueAndFalseQuestion(String text, int points, boolean answer) {
        super(text, points);
        this.answer = answer;
    }

    @Override
    public String toString() {
        // True/False Question: Question3 Points: 2 Answer: false

        return String.format("True/False Question: %s Points: %d Answer: %s",
                text, points, answer);
    }

    @Override
    float answer(String studentAnswer) {
        return answer == Boolean.parseBoolean(studentAnswer) ? points : (float) 0.0;
    }
}

class MultipleChoiceQuestion extends Question {

    private String answer;

    public MultipleChoiceQuestion(String text, int points, String answer) {
        super(text, points);
        this.answer = answer;
    }

    @Override
    public String toString() {
        // Multiple Choice Question: Question1 Points 3 Answer: E

        return String.format("Multiple Choice Question: %s Points %d Answer: %s",
                text, points, answer);
    }

    @Override
    float answer(String studentAnswer) {
        return answer.equals(studentAnswer) ? points : (float) (points * -0.2);
    }
}

abstract class Question implements Comparable<Question> {

    protected String text;
    protected int points;

    public Question(String text, int points) {
        this.text = text;
        this.points = points;
    }

    @Override
    public int compareTo(Question other) {
        return Integer.compare(this.points, other.points);
    }

    abstract float answer(String studentAnswer);
}

class QuestionFactory {

    private static final List<String> ALLOWED_ANSWERS = Arrays.asList("A", "B", "C", "D", "E");

    // TF;text;points;answer
    // MC;text;points;answer
    public static Question createQuestion(String line) throws InvalidOperationException {
        String[] parts = line.split(";");

        String typeOfQuestion = parts[0];
        String text = parts[1];
        int points = Integer.parseInt(parts[2]);
        String answer = parts[3];

        if (typeOfQuestion.equals("MC")) {
            if (!ALLOWED_ANSWERS.contains(answer)) {
                throw new InvalidOperationException(String.format("%s is not allowed option for this question", answer));
            }
            return new MultipleChoiceQuestion(text, points, answer);
        } else {
            return new TrueAndFalseQuestion(text, points, Boolean.parseBoolean(parts[3]));
        }
    }
}

class Quiz {

    private List<Question> questions;

    public Quiz() {
        this.questions = new ArrayList<>();
    }

    /* (додавање на прашање во квизот)
           TF;text;points;answer (answer може да биде true или false)
           MC;text;points;answer (answer е каракатер кој може да ја има вредноста A/B/C/D/E)
    */
    public void addQuestion(String questionData) throws InvalidOperationException {
        questions.add(QuestionFactory.createQuestion(questionData));
    }

    // сите прашања од квизот подредени според бројот на поени на прашањата во опаѓачки редослед
    public void printQuiz(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);

        questions.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(question -> writer.println(question.toString()));

        writer.flush();
    }

    public void answerQuiz(List<String> answers, OutputStream os) throws InvalidOperationException {
        PrintWriter writer = new PrintWriter(os);

        if (answers.size() != questions.size()) {
            throw new InvalidOperationException(String.format("Answers and questions must be of same length!"));
        }

        float sum = 0;

        for (int i = 0; i < answers.size(); i++) {
            float points = questions.get(i).answer(answers.get(i));
            writer.println(String.format("%d. %.2f",
                    i + 1, points));
            sum += points;
        }

        writer.println(String.format("Total points: %.2f",
                sum));

        writer.flush();
    }
}

public class QuizTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < questions; i++) {
            try {
                quiz.addQuestion(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<String> answers = new ArrayList<>();

        int answersCount = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < answersCount; i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) {
            quiz.printQuiz(System.out);
        } else if (testCase == 2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}