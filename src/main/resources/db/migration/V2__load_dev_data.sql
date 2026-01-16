INSERT INTO users (id, first_name, last_name, email, legal_id_type, legal_id)
VALUES ('ceccd113-a6d3-4b94-b52a-74e2af7654eb', 'Tyrion', 'Lannister', 'tyrion@mail.com', 'PASSPORT', '123456789'),
       ('e717bc1f-50e0-404e-92b9-3364570ccc8a', 'Jon', 'Snow', 'jon@mail.com', 'NATIONAL_ID', '321456789');

INSERT INTO properties (id, name, description, address, owner_id, check_in_time, check_out_time, nightly_price)
VALUES ('c3c27fcd-9536-46f9-83ad-5511bf1432fc', 'House Martell Sunspire Retreat',
        'Inspired by the warm elegance of Dorne, this sun-drenched apartment features airy spaces, golden tones, and a breezy balcony fit for a Dornish prince. A perfect refuge where “Unbowed, Unbent, Unbroken” meets ocean air and palm trees.',
        '1287 Bay Rd, Apt 502, Miami Beach, FL 33139', 'ceccd113-a6d3-4b94-b52a-74e2af7654eb', '14:00', '11:00', 80),
       ('d783e385-9e65-4f74-9e2b-ef0a1cc3e807', 'The Red Keep Residence',
        'A stately apartment inspired by King’s Landing’s most powerful office. Rich textures, classic details, and a quiet, commanding atmosphere make it ideal for plotting your next move in the city that never sleeps. Power rests here… temporarily.',
        '214 W 87th St, Apt 4A, New York, NY 10024', 'e717bc1f-50e0-404e-92b9-3364570ccc8a', '15:00', '10:00', 120);