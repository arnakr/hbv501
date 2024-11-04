package is.hi.hbv501g.hopur25.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all general exceptions that occur in the application.
     *
     * @param ex    The exception that occurred.
     * @param model The model to pass error information to the view.
     * @return The name of the error page to display.
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "error";  // Name of your error HTML page
    }

    /**
     * Handles 404 (Not Found) errors when no handler is found for a request.
     *
     * @param ex    The NoHandlerFoundException that occurred.
     * @param model The model to pass error information to the view.
     * @return The name of the error page to display.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorMessage", "The requested page was not found.");
        return "error";  // Name of your error HTML page
    }
}
