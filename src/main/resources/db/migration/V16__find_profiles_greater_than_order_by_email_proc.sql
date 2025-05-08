DELIMITER $$

CREATE PROCEDURE findProfilesGreaterThan(
    loyaltyPoint BIGINT
)
BEGIN
    SELECT p.id, p.bio, p.date_of_birth, p.loyalty_points, p.phone_number FROM profiles p
    JOIN users u ON u.id = p.id
    WHERE p.loyalty_points > loyaltyPoint
    ORDER BY u.email;
END $$

DELIMITER ;