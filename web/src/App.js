import './App.css';
import VideoItem from './videoItem/VideoItem';
import videos from './videoItem/videos'


function App() {
  const videoList = videos.map((video, key) => {
    return <VideoItem {...video} key={key}/>
  });

  return (
    <div className="container-fluid">
      <div className="row">
        <div className="col-3">
          <ul className="list-group">
            <li className="list-group-item d-flex align-items-center">
              <i className="bi bi-house-fill"></i>
              <span className="w-100 m-2 ms-3">Home</span>
              <span className="badge text-bg-primary rounded-pill">14</span>
            </li>
            <li className="list-group-item d-flex align-items-center">
              <i className="bi bi-search"></i>
              <span className="w-100 m-2 ms-3">Explore</span>
              <span className="badge text-bg-primary rounded-pill">2</span>
            </li>
            <li className="list-group-item d-flex align-items-center">
              <i className="bi bi-collection-play"></i>
              <span className="w-100 m-2 ms-3">Subscriptions</span>
              <span className="badge text-bg-primary rounded-pill">1</span>
            </li>
          </ul>
        </div>
        <div className="col-9">
          <div className="row"></div>
          <div className="row">
            <div>

            </div>
            <div>
              <div>
                
              </div>
            </div>
            <div>

            </div>
          </div>
          <div className="row g-2">
            {videoList}
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
