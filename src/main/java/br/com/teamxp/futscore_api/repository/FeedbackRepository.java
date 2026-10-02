package br.com.teamxp.futscore_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.teamxp.futscore_api.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}