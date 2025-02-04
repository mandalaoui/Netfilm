import './UserInfo.css';
import userIcon from '../../icons/user.png';
import { useGlobalContext } from '../../GlobalContext';

function UserInfo({ user, onClose }) {
    const { fileUrl } = useGlobalContext(); 
    if (!user) return null;

    const userPhoto = user.photo ? `${fileUrl}${user.photo}` : userIcon;


    return (
        <div className="user-info-modal-overlay" onClick={onClose}>
            <div className="user-info-modal" onClick={(e) => e.stopPropagation()}>
                <button className="user-info-close-btn" onClick={onClose}>X</button>
                <img src={userPhoto} alt="User" className="user-info-photo" />
                <h2>Hi, {user.nickname}!</h2>
                <p>Welcome to NetFilm. Here you can watch your favorite movies.</p>
                <p>Have a great time!</p>
            </div>
        </div>
    );
}

export default UserInfo;
