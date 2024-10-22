package SecondColloquium.ColloquiumAssignments._24MoviesList;

import java.util.*;
import java.util.stream.Collectors;

class Movie {

    private String movieTitle;
    private List<Integer> ratings;

    public Movie(String movieTitle, int[] ratings) {
        this.movieTitle = movieTitle;
        this.ratings = new ArrayList<>();
        for (int rating : ratings) {
            this.ratings.add(rating);
        }
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public double getAverageRating() {
        return ratings.stream()
                .mapToDouble(i -> i.doubleValue())
                .average()
                .orElse(0);
    }

    public int getRatingsCount() {
        return ratings.size();
    }

    // просечен ретјтинг на филмот x вкупно број на рејтинзи на филмот / максимален број на рејтинзи (од сите филмови во листата)
    public double getRatingCoefficient(int maxRatings) {
        return getAverageRating() * getRatingsCount() / (double) maxRatings;
    }

    @Override
    public String toString() {
        // Story of Women (1989) (6.63) of 8 ratings
        return String.format("%s (%.2f) of %d ratings",
                getMovieTitle(), getAverageRating(), getRatingsCount());
    }
}

class MoviesList {

    private List<Movie> movies;

    public MoviesList() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        movies.add(new Movie(title, ratings));
    }

    public List<Movie> top10ByAvgRating() {
        return movies.stream()
                .sorted((m1, m2) -> {
                    int result = Double.compare(m2.getAverageRating(), m1.getAverageRating());
                    if (result == 0) {
                        return m1.getMovieTitle().compareTo(m2.getMovieTitle());
                    }

                    return result;
                })
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef() {
        return movies.stream()
                .sorted((m1, m2) -> {
                    int result = Double.compare(m2.getRatingCoefficient(maxRatings()), m1.getRatingCoefficient(maxRatings()));
                    if (result == 0) {
                        return m1.getMovieTitle().compareTo(m2.getMovieTitle());
                    }

                    return result;
                })
                .limit(10)
                .collect(Collectors.toList());
    }

    private int maxRatings() {
        return movies.stream()
                .mapToInt(Movie::getRatingsCount)
                .max()
                .orElse(0);
    }
}

public class MoviesListTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}