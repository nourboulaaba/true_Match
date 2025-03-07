package tn.esprit.pitest11.Controller;

import tn.esprit.pitest11.Entities.Review;
import tn.esprit.pitest11.service.ReviewService;

import javax.ws.rs.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/reviews")
public class ReviewAPI {

    private ReviewService reviewService;

    public ReviewAPI() {
        this.reviewService = new ReviewService();
    }

    // Ajouter un avis sur une formation
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReview(Review review) {
        boolean isAdded = reviewService.addReview(review);

        if (isAdded) {
            return Response.status(Response.Status.CREATED).entity("Avis ajouté avec succès").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Erreur lors de l'ajout de l'avis").build();
        }
    }

    // Récupérer les avis d'une formation
    @GET
    @Path("/formation/{formationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewsByFormation(@PathParam("formationId") int formationId) {
        List<Review> reviews = reviewService.getReviewsByFormation(formationId);

        if (reviews.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).entity("Aucun avis trouvé pour cette formation").build();
        }

        try {
            // Sérialisation explicite avec ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(reviews);

            return Response.status(Response.Status.OK).entity(jsonResponse).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur lors de la sérialisation JSON").build();
        }
    }
}
