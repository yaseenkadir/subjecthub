/* THIS FILE IS CLOSED FOR EDITING. YOU SHOULD MAKE CHANGES IN A NEW SQL FILE */

ALTER TABLE universities ADD image_url VARCHAR(500) NOT NULL DEFAULT 'foo';

/*
If we were actually running a live instance we would have to migrate the existing options.
Because we want to ensure that image urls are not null, we would have to set a default, migrate
the existing rows one by one and then remove the default constraint.
 */

-- UPDATE universities SET
-- image_url='https://s3-ap-southeast-2.amazonaws.com/wordpress.multisite.prod.uploads/wp-content/uploads/sites/4/2017/07/10125839/UTSLogo_Horizontal_BLK.png'
-- WHERE university_id=1;
--
-- UPDATE universities SET
-- image_url='https://www.uow.edu.au/content/groups/public/@web/documents/siteelement/uow171491.png'
-- WHERE university_id=2;

ALTER TABLE universities ALTER COLUMN image_url DROP DEFAULT;

/* THIS FILE IS CLOSED FOR EDITING. YOU SHOULD MAKE CHANGES IN A NEW SQL FILE */


