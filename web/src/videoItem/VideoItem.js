import img1 from '../data/pictures/img1.jpg';

function VideoItem({title, author}) {
    return (
        <div className="card col-xl-2 col-lg-3 col-md-4 col-sm-6 p-1">
            <img src={img1} className="card-img-top" alt="..."></img>
            <div className="card-body">
                <p className="card-text">{title}</p>
                <p className="card-text">{author}</p>
                <p className="card-text">100 views</p>
            </div>
        </div>
    );
}

export default VideoItem;