import './View.css';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

function View({ movie }) {
        console.log(movie);
        const [isPlaying, setIsPlaying] = useState(false);
        const videoSrc = `http://localhost:12345/api/${movie.video}`;
        console.log(videoSrc);
        const navigate = useNavigate();

        useEffect(() => {
                const video = document.querySelector('video');
                if (video) {
                        video.oncanplay = () => {
                        };
                }
        }, []);

        const handlePlay = () => {
                const video = document.querySelector('video');
                if (video) {
                        video.muted = false;
                        video.play();
                        setIsPlaying(true); 
                }
        };

        const goToHome = () => {
                navigate('../../home');
            };

        return (
                <div className="video-container">
                        <button className="back-button" onClick={goToHome}>
                                <i className="bi bi-arrow-left"></i>
                        </button>                        
                        <video className="video-player" controls muted>
                                <source src={videoSrc} type="video/mp4" />
                        </video>
                        {!isPlaying && (
                                <button className="main-play-button" onClick={handlePlay}></button>
                        )}
                </div>
        );
}


export default View;
