import React, { useState, useEffect, useRef } from 'react';
import { MapPin, Navigation, Clock, Activity, Zap, TrendingUp, Settings, Users, Download, Play, Pause, RotateCcw } from 'lucide-react';

const ProfessionalGPSTracker = () => {
  const [position, setPosition] = useState(null);
  const [tracking, setTracking] = useState(false);
  const [route, setRoute] = useState([]);
  const [stats, setStats] = useState({
    distance: 0,
    duration: 0,
    avgSpeed: 0,
    maxSpeed: 0
  });
  const [error, setError] = useState(null);
  const watchIdRef = useRef(null);
  const startTimeRef = useRef(null);
  const lastPositionRef = useRef(null);

  useEffect(() => {
    if ('geolocation' in navigator) {
      navigator.geolocation.getCurrentPosition(
        (pos) => {
          setPosition({
            lat: pos.coords.latitude,
            lng: pos.coords.longitude,
            accuracy: pos.coords.accuracy,
            speed: pos.coords.speed,
            heading: pos.coords.heading,
            timestamp: pos.timestamp
          });
          setError(null);
        },
        (err) => setError(err.message),
        { enableHighAccuracy: true }
      );
    } else {
      setError('Geolocation not supported');
    }
  }, []);

  const calculateDistance = (lat1, lon1, lat2, lon2) => {
    const R = 6371;
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
              Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
              Math.sin(dLon/2) * Math.sin(dLon/2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return R * c;
  };

  const startTracking = () => {
    if (!('geolocation' in navigator)) {
      setError('Geolocation not supported');
      return;
    }

    setTracking(true);
    startTimeRef.current = Date.now();
    setRoute([]);
    setStats({ distance: 0, duration: 0, avgSpeed: 0, maxSpeed: 0 });

    watchIdRef.current = navigator.geolocation.watchPosition(
      (pos) => {
        const newPos = {
          lat: pos.coords.latitude,
          lng: pos.coords.longitude,
          accuracy: pos.coords.accuracy,
          speed: pos.coords.speed || 0,
          heading: pos.coords.heading,
          timestamp: pos.timestamp
        };

        setPosition(newPos);
        setRoute(prev => [...prev, newPos]);

        if (lastPositionRef.current) {
          const dist = calculateDistance(
            lastPositionRef.current.lat,
            lastPositionRef.current.lng,
            newPos.lat,
            newPos.lng
          );

          setStats(prev => {
            const newDistance = prev.distance + dist;
            const duration = (Date.now() - startTimeRef.current) / 1000;
            const avgSpeed = duration > 0 ? (newDistance / duration) * 3600 : 0;
            const maxSpeed = Math.max(prev.maxSpeed, (newPos.speed || 0) * 3.6);

            return {
              distance: newDistance,
              duration,
              avgSpeed,
              maxSpeed
            };
          });
        }

        lastPositionRef.current = newPos;
        setError(null);
      },
      (err) => setError(err.message),
      {
        enableHighAccuracy: true,
        maximumAge: 0,
        timeout: 5000
      }
    );
  };

  const stopTracking = () => {
    if (watchIdRef.current !== null) {
      navigator.geolocation.clearWatch(watchIdRef.current);
      watchIdRef.current = null;
    }
    setTracking(false);
    lastPositionRef.current = null;
  };

  const resetTracking = () => {
    stopTracking();
    setRoute([]);
    setStats({ distance: 0, duration: 0, avgSpeed: 0, maxSpeed: 0 });
    startTimeRef.current = null;
  };

  const exportData = () => {
    const data = {
      route,
      stats,
      exportDate: new Date().toISOString(),
      totalPoints: route.length
    };
    const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `gps-track-${Date.now()}.json`;
    a.click();
    URL.revokeObjectURL(url);
  };

  const formatDuration = (seconds) => {
    const h = Math.floor(seconds / 3600);
    const m = Math.floor((seconds % 3600) / 60);
    const s = Math.floor(seconds % 60);
    return `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-blue-900 to-slate-900">
      {/* Header */}
      <div className="bg-slate-900/80 backdrop-blur-sm border-b border-blue-500/30 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-3">
              <div className="bg-gradient-to-br from-blue-500 to-cyan-500 p-2 rounded-lg">
                <Navigation className="w-6 h-6 text-white" />
              </div>
              <div>
                <h1 className="text-2xl font-bold text-white">GPS Tracking System</h1>
                <p className="text-sm text-blue-300">Enterprise-Grade Location Intelligence</p>
              </div>
            </div>
            <div className="flex items-center space-x-2">
              <button className="p-2 rounded-lg bg-slate-800/50 hover:bg-slate-700/50 text-blue-400 transition-all">
                <Settings className="w-5 h-5" />
              </button>
              <button className="p-2 rounded-lg bg-slate-800/50 hover:bg-slate-700/50 text-blue-400 transition-all">
                <Users className="w-5 h-5" />
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Error Display */}
        {error && (
          <div className="mb-6 bg-red-500/20 border border-red-500/50 rounded-lg p-4">
            <p className="text-red-300 text-sm">⚠️ {error}</p>
          </div>
        )}

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
          <div className="bg-gradient-to-br from-blue-500/20 to-blue-600/20 backdrop-blur-sm border border-blue-500/30 rounded-xl p-6">
            <div className="flex items-center justify-between mb-2">
              <div className="bg-blue-500/20 p-2 rounded-lg">
                <TrendingUp className="w-5 h-5 text-blue-400" />
              </div>
              <span className="text-xs text-blue-300 font-medium">DISTANCE</span>
            </div>
            <div className="text-3xl font-bold text-white mb-1">
              {stats.distance.toFixed(2)}
            </div>
            <div className="text-sm text-blue-300">kilometers</div>
          </div>

          <div className="bg-gradient-to-br from-cyan-500/20 to-cyan-600/20 backdrop-blur-sm border border-cyan-500/30 rounded-xl p-6">
            <div className="flex items-center justify-between mb-2">
              <div className="bg-cyan-500/20 p-2 rounded-lg">
                <Clock className="w-5 h-5 text-cyan-400" />
              </div>
              <span className="text-xs text-cyan-300 font-medium">DURATION</span>
            </div>
            <div className="text-3xl font-bold text-white mb-1">
              {formatDuration(stats.duration)}
            </div>
            <div className="text-sm text-cyan-300">hours:min:sec</div>
          </div>

          <div className="bg-gradient-to-br from-green-500/20 to-green-600/20 backdrop-blur-sm border border-green-500/30 rounded-xl p-6">
            <div className="flex items-center justify-between mb-2">
              <div className="bg-green-500/20 p-2 rounded-lg">
                <Activity className="w-5 h-5 text-green-400" />
              </div>
              <span className="text-xs text-green-300 font-medium">AVG SPEED</span>
            </div>
            <div className="text-3xl font-bold text-white mb-1">
              {stats.avgSpeed.toFixed(1)}
            </div>
            <div className="text-sm text-green-300">km/h</div>
          </div>

          <div className="bg-gradient-to-br from-purple-500/20 to-purple-600/20 backdrop-blur-sm border border-purple-500/30 rounded-xl p-6">
            <div className="flex items-center justify-between mb-2">
              <div className="bg-purple-500/20 p-2 rounded-lg">
                <Zap className="w-5 h-5 text-purple-400" />
              </div>
              <span className="text-xs text-purple-300 font-medium">MAX SPEED</span>
            </div>
            <div className="text-3xl font-bold text-white mb-1">
              {stats.maxSpeed.toFixed(1)}
            </div>
            <div className="text-sm text-purple-300">km/h</div>
          </div>
        </div>

        {/* Main Content */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Map Visualization */}
          <div className="lg:col-span-2 bg-slate-800/50 backdrop-blur-sm border border-blue-500/30 rounded-xl p-6">
            <h2 className="text-xl font-semibold text-white mb-4 flex items-center">
              <MapPin className="w-5 h-5 mr-2 text-blue-400" />
              Live Location Map
            </h2>
            <div className="bg-gradient-to-br from-slate-900 to-slate-800 rounded-lg h-96 flex items-center justify-center relative overflow-hidden border border-blue-500/20">
              {position ? (
                <div className="absolute inset-0 flex items-center justify-center">
                  <div className="relative">
                    {/* Pulsing effect */}
                    <div className="absolute inset-0 animate-ping">
                      <div className="w-24 h-24 bg-blue-500/30 rounded-full"></div>
                    </div>
                    {/* Center marker */}
                    <div className="relative z-10 flex flex-col items-center">
                      <div className="bg-gradient-to-br from-blue-500 to-cyan-500 p-4 rounded-full shadow-lg shadow-blue-500/50">
                        <MapPin className="w-8 h-8 text-white" />
                      </div>
                      <div className="mt-4 bg-slate-900/90 backdrop-blur-sm px-4 py-2 rounded-lg border border-blue-500/30">
                        <p className="text-xs text-blue-300 font-mono">
                          {position.lat.toFixed(6)}, {position.lng.toFixed(6)}
                        </p>
                        <p className="text-xs text-slate-400 text-center mt-1">
                          Accuracy: ±{position.accuracy.toFixed(0)}m
                        </p>
                      </div>
                    </div>
                  </div>
                  
                  {/* Route visualization */}
                  {route.length > 1 && (
                    <div className="absolute top-4 right-4 bg-slate-900/90 backdrop-blur-sm px-4 py-2 rounded-lg border border-blue-500/30">
                      <p className="text-sm text-white font-semibold">{route.length} points tracked</p>
                    </div>
                  )}
                </div>
              ) : (
                <div className="text-center">
                  <div className="animate-pulse mb-4">
                    <MapPin className="w-16 h-16 text-blue-500/50 mx-auto" />
                  </div>
                  <p className="text-slate-400">Waiting for GPS signal...</p>
                </div>
              )}
            </div>
          </div>

          {/* Control Panel */}
          <div className="space-y-6">
            {/* Controls */}
            <div className="bg-slate-800/50 backdrop-blur-sm border border-blue-500/30 rounded-xl p-6">
              <h2 className="text-xl font-semibold text-white mb-4">Controls</h2>
              <div className="space-y-3">
                {!tracking ? (
                  <button
                    onClick={startTracking}
                    className="w-full bg-gradient-to-r from-green-500 to-emerald-500 hover:from-green-600 hover:to-emerald-600 text-white font-semibold py-3 px-4 rounded-lg transition-all transform hover:scale-105 flex items-center justify-center shadow-lg shadow-green-500/30"
                  >
                    <Play className="w-5 h-5 mr-2" />
                    Start Tracking
                  </button>
                ) : (
                  <button
                    onClick={stopTracking}
                    className="w-full bg-gradient-to-r from-red-500 to-rose-500 hover:from-red-600 hover:to-rose-600 text-white font-semibold py-3 px-4 rounded-lg transition-all transform hover:scale-105 flex items-center justify-center shadow-lg shadow-red-500/30"
                  >
                    <Pause className="w-5 h-5 mr-2" />
                    Stop Tracking
                  </button>
                )}
                
                <button
                  onClick={resetTracking}
                  className="w-full bg-gradient-to-r from-slate-600 to-slate-700 hover:from-slate-700 hover:to-slate-800 text-white font-semibold py-3 px-4 rounded-lg transition-all flex items-center justify-center"
                  disabled={route.length === 0}
                >
                  <RotateCcw className="w-5 h-5 mr-2" />
                  Reset
                </button>
                
                <button
                  onClick={exportData}
                  className="w-full bg-gradient-to-r from-blue-500 to-cyan-500 hover:from-blue-600 hover:to-cyan-600 text-white font-semibold py-3 px-4 rounded-lg transition-all flex items-center justify-center shadow-lg shadow-blue-500/30"
                  disabled={route.length === 0}
                >
                  <Download className="w-5 h-5 mr-2" />
                  Export Data
                </button>
              </div>
            </div>

            {/* Current Position Details */}
            {position && (
              <div className="bg-slate-800/50 backdrop-blur-sm border border-blue-500/30 rounded-xl p-6">
                <h2 className="text-xl font-semibold text-white mb-4">Position Details</h2>
                <div className="space-y-3">
                  <div>
                    <p className="text-xs text-slate-400 mb-1">Latitude</p>
                    <p className="text-sm text-white font-mono bg-slate-900/50 px-3 py-2 rounded">
                      {position.lat.toFixed(6)}°
                    </p>
                  </div>
                  <div>
                    <p className="text-xs text-slate-400 mb-1">Longitude</p>
                    <p className="text-sm text-white font-mono bg-slate-900/50 px-3 py-2 rounded">
                      {position.lng.toFixed(6)}°
                    </p>
                  </div>
                  <div>
                    <p className="text-xs text-slate-400 mb-1">Current Speed</p>
                    <p className="text-sm text-white font-mono bg-slate-900/50 px-3 py-2 rounded">
                      {position.speed ? (position.speed * 3.6).toFixed(1) : '0.0'} km/h
                    </p>
                  </div>
                  {position.heading !== null && (
                    <div>
                      <p className="text-xs text-slate-400 mb-1">Heading</p>
                      <p className="text-sm text-white font-mono bg-slate-900/50 px-3 py-2 rounded">
                        {position.heading.toFixed(0)}°
                      </p>
                    </div>
                  )}
                  <div>
                    <p className="text-xs text-slate-400 mb-1">Last Update</p>
                    <p className="text-sm text-white font-mono bg-slate-900/50 px-3 py-2 rounded">
                      {new Date(position.timestamp).toLocaleTimeString()}
                    </p>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfessionalGPSTracker;
