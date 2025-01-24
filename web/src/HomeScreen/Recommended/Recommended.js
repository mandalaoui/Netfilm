import './Recommended.css';
import videoSrc from '../../data/movies/horse.mp4'
import { useEffect } from 'react';

function Recommended() {

    useEffect(() => {
        const video = document.querySelector('video');
        video.play();
    }, []);

    return (
        <div>
            <div className="video-recommended-container">
                <div className="video-recommended">
                    <video autoplay muted loop>
                        <source src={videoSrc} type="video/mp4" />
                    </video>
                </div>
            </div>
        </div>
    );
}

export default Recommended;
