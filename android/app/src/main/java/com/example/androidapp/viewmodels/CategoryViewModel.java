//import android.app.Application;
//
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//
//import com.example.androidapp.Category;
//
//import java.util.List;
//
//public class CategoryViewModel extends AndroidViewModel {
//    private CategoryRepository categoryRepository;
//    private LiveData<List<Category>> allCategories;
//
//    public CategoryViewModel(Application application) {
//        super(application);
//        categoryRepository = new CategoryRepository(application);
//        allCategories = categoryRepository.getAllCategories();
//    }
//
//    public LiveData<List<Category>> getAllCategories() {
//        return allCategories;
//    }
//
//    public void insertCategories(List<Category> categories) {
//        categoryRepository.insertCategories(categories);
//    }
//}
