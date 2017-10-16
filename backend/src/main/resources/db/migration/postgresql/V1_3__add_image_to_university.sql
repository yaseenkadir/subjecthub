/* THIS FILE IS CLOSED FOR EDITING. YOU SHOULD MAKE CHANGES IN A NEW SQL FILE */

/* set everything to foo and update entries individually */
ALTER TABLE universities ADD COLUMN image_url VARCHAR(500) DEFAULT 'foo' NOT NULL;

UPDATE universities
SET image_url='https://s3-ap-southeast-2.amazonaws.com/wordpress.multisite.prod.uploads/wp-content/uploads/sites/4/2017/07/10125839/UTSLogo_Horizontal_BLK.png'
WHERE name='University of Technology Sydney';

UPDATE universities
SET image_url='https://www.uow.edu.au/content/groups/public/@web/documents/siteelement/uow171491.png'
WHERE name='University of Wollongong';

ALTER TABLE universities ALTER COLUMN image_url DROP DEFAULT;

/* THIS FILE IS CLOSED FOR EDITING. YOU SHOULD MAKE CHANGES IN A NEW SQL FILE */
