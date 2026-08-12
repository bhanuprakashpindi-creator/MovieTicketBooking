package com.bhanu.movie_booking_system.transformer;

import com.bhanu.movie_booking_system.dto.request.ShowRequest;
import com.bhanu.movie_booking_system.dto.response.ShowResponse;
import com.bhanu.movie_booking_system.entity.Enum.ShowStatus;
import com.bhanu.movie_booking_system.entity.Show;

public class ShowTransformer {

    public static Show showRequestToShow(ShowRequest showRequest) {

        Show show = new Show();

        show.setShowDateTime(showRequest.getShowDateTime());
        show.setTicketPrice(showRequest.getTicketPrice());

        // Default status while creating a show
        show.setShowStatus(ShowStatus.SCHEDULED);

        return show;
    }

    public static ShowResponse showToShowResponse(Show show) {

        ShowResponse response = new ShowResponse();

        response.setShowId(show.getShowId());

        response.setMovieId(show.getMovie().getMovieId());
        response.setMovieTitle(show.getMovie().getMovieTitle());

        response.setScreenId(show.getScreen().getScreenId());
        response.setScreenName(show.getScreen().getScreenName());

        response.setShowDateTime(show.getShowDateTime());

        response.setTicketPrice(show.getTicketPrice());

        response.setShowStatus(show.getShowStatus());

        return response;
    }
}