//import android.app.Application;
//import android.os.AsyncTask;
//
//import androidx.lifecycle.LiveData;
//
//import com.example.androidapp.Category;
//import com.example.androidapp.CategoryDB;
//import com.example.androidapp.dao.CategoryDao;
//
//import java.util.List;
//
//public class CategoryRepository {
//    private CategoryDao categoryDao;
//    private LiveData<List<Category>> allCategories;
//
//    public CategoryRepository(Application application) {
//        CategoryDB database = CategoryDB.getInstance(application);
//        categoryDao = database.categoryDao();
//        allCategories = categoryDao.getAllCategories();
//    }
//
//    public LiveData<List<Category>> getAllCategories() {
//        return allCategories;
//    }
//
//    public void insertCategories(List<Category> categories) {
//        new InsertCategoriesAsyncTask(categoryDao).execute(categories);
//    }
//
//    // AsyncTask להוספת קטגוריות לבסיס הנתונים
//    private static class InsertCategoriesAsyncTask extends AsyncTask<List<Category>, Void, Void> {
//        private CategoryDao categoryDao;
//
//        private InsertCategoriesAsyncTask(CategoryDao categoryDao) {
//            this.categoryDao = categoryDao;
//        }
//
//        @Override
//        protected Void doInBackground(List<Category>... lists) {
//            categoryDao.insertCategories(lists[0]);
//            return null;
//        }
//    }
//}