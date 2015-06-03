import org.junit.Test;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DBTest {

    @Test
    public void clickingButton_shouldChangeResultsViewText() throws Exception {


        Schema schema = new Schema(4, "jyu.secret.model");
        Entity entity = schema.addEntity("Secret");

        entity.addLongProperty("id").index().primaryKey().autoincrement().unique();
        entity.addStringProperty("title").index().unique();
        entity.addLongProperty("userId");
        entity.addStringProperty("name");
        entity.addLongProperty("level").index();
        entity.addStringProperty("pwd");
        entity.addDateProperty("createdDate");
        entity.addDateProperty("updatedDate");

        Entity entity2 = schema.addEntity("User");


        entity2.addLongProperty("id").index().primaryKey().autoincrement().unique();
        entity2.addStringProperty("name").index().unique();
        entity2.addStringProperty("pwd");
        entity2.addDateProperty("createdDate");
        entity2.addDateProperty("updatedDate");


        new DaoGenerator().generateAll(schema, "./src/main/java");
    }

}