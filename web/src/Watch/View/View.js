import './View.css';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

function View({ movie }) {
        const [isPlaying, setIsPlaying] = useState(false);  // Track if the video is playing
        const videoSrc = `${getComputedStyle(document.documentElement).getPropertyValue('--FILE_URL')}${movie}`; // Movie video source
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
                        // video.muted = false;  // Unmute the video
                        video.play(); // Start playing the video
                        setIsPlaying(true); // Update state to reflect the video is playing
                }
        };

        const goToHome = () => {
                navigate('../../home'); // Navigate to the home screen
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
