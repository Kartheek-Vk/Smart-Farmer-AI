-- Reference catalogue of widely grown Indian crops so the crop APIs are usable out of the box.
INSERT INTO crops (id, created_at, updated_at, name, category, description, season)
VALUES
    (gen_random_uuid(), now(), now(), 'Rice', 'CEREAL', 'Staple cereal grown in flooded fields.', 'KHARIF'),
    (gen_random_uuid(), now(), now(), 'Wheat', 'CEREAL', 'Rabi cereal sown after the monsoon.', 'RABI'),
    (gen_random_uuid(), now(), now(), 'Maize', 'CEREAL', 'Grown for grain, fodder and starch.', 'KHARIF'),
    (gen_random_uuid(), now(), now(), 'Sorghum', 'MILLET', 'Drought tolerant millet also known as jowar.', 'KHARIF'),
    (gen_random_uuid(), now(), now(), 'Pearl Millet', 'MILLET', 'Bajra, tolerant of arid conditions.', 'KHARIF'),
    (gen_random_uuid(), now(), now(), 'Chickpea', 'PULSE', 'Gram, a major rabi pulse.', 'RABI'),
    (gen_random_uuid(), now(), now(), 'Pigeon Pea', 'PULSE', 'Tur or arhar, a long duration pulse.', 'KHARIF'),
    (gen_random_uuid(), now(), now(), 'Groundnut', 'OILSEED', 'Peanut, grown for oil and food.', 'KHARIF'),
    (gen_random_uuid(), now(), now(), 'Mustard', 'OILSEED', 'Rabi oilseed grown across north India.', 'RABI'),
    (gen_random_uuid(), now(), now(), 'Soybean', 'OILSEED', 'Protein and oil rich legume.', 'KHARIF'),
    (gen_random_uuid(), now(), now(), 'Cotton', 'FIBRE', 'Long duration fibre crop.', 'KHARIF'),
    (gen_random_uuid(), now(), now(), 'Sugarcane', 'CASH', 'Perennial cash crop for sugar and jaggery.', 'ANNUAL'),
    (gen_random_uuid(), now(), now(), 'Potato', 'VEGETABLE', 'Tuber crop grown mainly in the rabi season.', 'RABI'),
    (gen_random_uuid(), now(), now(), 'Tomato', 'VEGETABLE', 'Grown year round under irrigation.', 'ANNUAL'),
    (gen_random_uuid(), now(), now(), 'Onion', 'VEGETABLE', 'Bulb crop with kharif and rabi cycles.', 'ANNUAL'),
    (gen_random_uuid(), now(), now(), 'Banana', 'FRUIT', 'Perennial fruit crop needing regular irrigation.', 'ANNUAL'),
    (gen_random_uuid(), now(), now(), 'Mango', 'FRUIT', 'Orchard fruit harvested in summer.', 'ANNUAL'),
    (gen_random_uuid(), now(), now(), 'Turmeric', 'SPICE', 'Rhizome spice with a long growing period.', 'KHARIF')
ON CONFLICT (name) DO NOTHING;
